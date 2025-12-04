# Docker Deployment Guide

This guide covers running the User Pet API application using Docker and Docker Compose.

## Overview

The application consists of two services:
- **Backend**: Java Spring Boot API (port 8080)
- **Frontend**: React/TypeScript application (port 3000/80)

Both services are containerized and can be orchestrated using Docker Compose.

## Prerequisites

- Docker 20.10 or higher
- Docker Compose 2.0 or higher

## Quick Start

### Run the Full Application

From the project root directory:

```bash
# Build and start both services
docker-compose up --build

# Or run in detached mode
docker-compose up --build -d
```

The services will be available at:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/users-with-pet

### Stop the Application

```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## Individual Service Management

### Backend Service

Build and run the backend independently:

```bash
# Navigate to backend directory
cd backend

# Build the Docker image
docker build -t users-pets-backend .

# Run the container
docker run -p 8080:8080 --name backend users-pets-backend

# Test the API
curl http://localhost:8080/api/users-with-pet?results=10
```

### Frontend Service

The frontend already has its own Docker setup:

```bash
# Navigate to frontend directory
cd frontend

# Build and run using docker-compose
docker-compose up --build

# Or build and run manually
docker build -t users-pets-frontend .
docker run -p 3000:80 --name frontend users-pets-frontend
```

## Docker Compose Configuration

The root `docker-compose.yml` orchestrates both services:

```yaml
services:
  backend:    # Java Spring Boot API on port 8080
  frontend:   # React app served via Nginx on port 3000
```

### Features:
- **Networking**: Both services share a common bridge network (`users-pets-network`)
- **Health Checks**: Backend includes health check endpoint monitoring
- **Restart Policy**: Services automatically restart unless stopped
- **Dependencies**: Frontend waits for backend to start

## Development vs Production

### Production (Default)

```bash
docker-compose up --build
```

### Development Mode

For development with hot-reloading:

```bash
cd frontend
docker-compose -f docker-compose.dev.yml up
```

## Environment Variables

### Backend

The backend accepts standard Spring Boot environment variables:

```yaml
environment:
  - SPRING_PROFILES_ACTIVE=production
  - SERVER_PORT=8080  # Optional: change port
```

### Frontend

```yaml
environment:
  - NODE_ENV=production
```

## Volumes (Optional)

For development, you can mount volumes to enable hot-reloading:

```yaml
volumes:
  - ./backend/src:/app/src
  - ./frontend/src:/app/src
```

## Networking

Both services communicate via the `users-pets-network` bridge network:
- Backend is accessible at `http://backend:8080` from within the network
- Frontend is accessible at `http://frontend:80` from within the network

## Health Checks

The backend includes a health check that:
- Tests the API endpoint every 30 seconds
- Has a 40-second startup grace period
- Retries 3 times before marking as unhealthy

Check service health:

```bash
docker-compose ps
```

## Troubleshooting

### Port Conflicts

If ports 8080 or 3000 are in use, modify `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # Change host port
  - "3001:80"    # Change host port
```

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Rebuild Services

```bash
# Rebuild all services
docker-compose build --no-cache

# Rebuild specific service
docker-compose build --no-cache backend
```

### Clean Up

```bash
# Remove all containers, networks, and images
docker-compose down --rmi all

# Remove volumes as well
docker-compose down -v --rmi all
```

## Image Sizes

The multi-stage builds keep images lean:
- **Backend**: ~200-300 MB (JRE Alpine-based)
- **Frontend**: ~25-30 MB (Nginx Alpine-based)

## Production Deployment

For production deployment:

1. **Use Docker Swarm or Kubernetes** for orchestration
2. **Add environment-specific configs** via `.env` files
3. **Implement SSL/TLS** using reverse proxy (Nginx, Traefik)
4. **Set resource limits** in docker-compose:

```yaml
deploy:
  resources:
    limits:
      cpus: '1'
      memory: 512M
```

5. **Use Docker secrets** for sensitive data
6. **Enable logging drivers** for centralized logging

## CI/CD Integration

Example GitHub Actions workflow:

```yaml
- name: Build Docker images
  run: docker-compose build

- name: Run tests
  run: docker-compose run backend ./mvnw test

- name: Push to registry
  run: |
    docker tag users-pets-backend registry.example.com/backend
    docker push registry.example.com/backend
```

## API Testing

Once running, test the backend API:

```bash
# Get 10 users
curl http://localhost:8080/api/users-with-pet?results=10

# Get Finnish users
curl http://localhost:8080/api/users-with-pet?results=20&nat=FI

# Get American users
curl http://localhost:8080/api/users-with-pet?results=50&nat=US
```

## Additional Commands

```bash
# Scale services (if needed)
docker-compose up --scale backend=2

# Execute commands in running container
docker-compose exec backend sh

# View resource usage
docker stats

# Inspect network
docker network inspect users-pets-network
```

## Notes

- The backend uses a **fixed seed** (`aimopark2025`) for deterministic results
- Both services use **Alpine Linux** base images for minimal size
- The frontend is served via **Nginx** in production mode
- Multi-stage builds ensure only production artifacts are in final images
