# Docker Setup

This document provides instructions for running the Users & Their Pets React application using Docker.

## Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+

## Available Docker Configurations

### 1. Production Setup

For production deployment with optimized build:

```bash
docker-compose up --build
```

The application will be available at `http://localhost:3000`

### 2. Development Setup

For development with hot-reload:

```bash
docker-compose -f docker-compose.dev.yml up --build
```

The application will be available at `http://localhost:3001`

## Docker Commands

### Build and Start Production Container
```bash
docker-compose up --build -d
```

### Build and Start Development Container
```bash
docker-compose -f docker-compose.dev.yml up --build -d
```

### Stop Containers
```bash
docker-compose down
docker-compose -f docker-compose.dev.yml down
```

### View Logs
```bash
docker-compose logs -f
docker-compose -f docker-compose.dev.yml logs -f
```

### Remove Containers and Images
```bash
docker-compose down --rmi all
docker-compose -f docker-compose.dev.yml down --rmi all
```

## Docker Files

- `Dockerfile` - Multi-stage production build with Nginx
- `Dockerfile.dev` - Development setup with hot-reload
- `docker-compose.yml` - Production service configuration
- `docker-compose.dev.yml` - Development service configuration
- `.dockerignore` - Files to exclude from Docker build context
- `nginx.conf` - Nginx configuration for serving the React app

## Production Build Features

- Multi-stage build for optimized image size
- Nginx server with:
  - Client-side routing support
  - Static asset caching
  - Security headers
  - Gzip compression

## Development Features

- Hot reload for instant code changes
- Volume mounting for live development
- Interactive terminal support

## Environment Variables

The application supports the following environment variables in production:

- `NODE_ENV` - Set to `production` in production containers
- Custom environment variables can be added to `docker-compose.yml`

## Troubleshooting

### Port Already in Use
If port 3000 or 3001 is already in use, modify the port mapping in the respective docker-compose file:

```yaml
ports:
  - "3002:80"  # Change 3002 to any available port
```

### Container Won't Start
Check the logs for detailed error information:
```bash
docker-compose logs frontend
```

### Permission Issues (Linux/Mac)
If you encounter permission issues, ensure your user is in the docker group:
```bash
sudo usermod -aG docker $USER
```

## Production Deployment

For production deployment, consider:

1. Using environment-specific docker-compose files
2. Implementing proper logging and monitoring
3. Setting up SSL/TLS certificates
4. Configuring proper backup strategies
5. Using secrets management for sensitive data