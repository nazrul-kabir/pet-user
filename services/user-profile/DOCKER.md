# Docker Deployment Guide

## Quick Start

### Prerequisites
- Docker Desktop installed and running
- Open port 8081 on host machine

### Build and Run Commands

```bash
# Build the Docker image
docker build -t user-profile:latest .

# Run the container
docker run -d \
  --name user-profile-service \
  -p 8081:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  user-profile:latest
```

### Using Docker Compose (Recommended)

```bash
# Build and start the service
docker-compose up --build

# Stop the service
docker-compose down

# View logs
docker-compose logs -f user-profile
```

## Container Features

✅ **Multi-stage build** - Small final image size (~180MB)  
✅ **Non-root user** - Enhanced security  
✅ **Health checks** - Built-in monitoring via /actuator/health  
✅ **Production configuration** - Optimized for production  
✅ **Resource limits** - Memory management configured  
✅ **Layer optimization** - Faster rebuilds with dependency caching  

## Access Endpoints

- **API**: http://localhost:8081/api/v1/users
- **Health**: http://localhost:8081/actuator/health
- **Info**: http://localhost:8081/actuator/info

## Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE` - Set to 'prod' for production mode
- `JAVA_OPTS` - JVM options (default: -Xmx512m -Xms256m)

### Ports
- Host: 8081
- Container: 8080

## Monitoring

### Health Check
```bash
curl http://localhost:8081/actuator/health
```

### Application Logs
```bash
docker-compose logs -f user-profile
# or
docker logs -f user-profile-service
```

## Production Deployment Tips

1. **Resource Limits**: Adjust memory based on load
2. **Reverse Proxy**: Use Nginx/traefik for HTTPS
3. **Monitoring**: Integrate with Prometheus/Grafana
4. **Scaling**: Use Docker Swarm or Kubernetes
5. **Security**: Add secrets management for sensitive data

## Troubleshooting

### Container fails to start
```bash
# Check logs
docker logs user-profile-service

# Check health status
docker inspect user-profile-service --format='{{.State.Health.Status}}'
```

### Port conflicts
Change host port mapping:
```yaml
# In docker-compose.yml
ports:
  - "9081:8080"  # Use 9081 instead of 8081
```

### Build issues
```bash
# Clean build
docker-compose down --rmi all
docker-compose up --build --force-recreate
```