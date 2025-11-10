# Docker Deployment Guide

This guide covers running the Restaurant Menu Service using Docker and Docker Compose.

## Prerequisites

- Docker 20.10+
- Docker Compose 2.0+ (optional, for docker-compose setup)

## Quick Start with Docker Compose (Recommended)

### 1. Build and Start All Services

```bash
docker-compose up -d
```

This will:

- Start MongoDB container
- Build the Restaurant Menu Service image
- Start the service container
- Create a network for inter-container communication
- Set up persistent volume for MongoDB data

### 2. Check Status

```bash
docker-compose ps
```

### 3. View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f restaurant-menu-service
```

### 4. Stop Services

```bash
docker-compose down
```

### 5. Stop and Remove Volumes

```bash
docker-compose down -v
```

## Manual Docker Setup

### 1. Start MongoDB

```bash
docker run -d \
  --name mongodb \
  -p 27017:27017 \
  -v mongodb_data:/data/db \
  mongo:latest
```

### 2. Build the Service Image

```bash
# Make sure you have built the JAR first
mvn clean package -DskipTests

# Build Docker image
docker build -t restaurant-menu-service .
```

### 3. Run the Service

```bash
docker run -d \
  --name restaurant-menu-service \
  -p 8085:8085 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017/restaurant-db \
  restaurant-menu-service
```

### 4. Check Logs

```bash
docker logs -f restaurant-menu-service
```

### 5. Stop and Remove Containers

```bash
docker stop restaurant-menu-service mongodb
docker rm restaurant-menu-service mongodb
```

## Docker Commands Reference

### Container Management

```bash
# List running containers
docker ps

# List all containers
docker ps -a

# Start a stopped container
docker start restaurant-menu-service

# Stop a running container
docker stop restaurant-menu-service

# Restart a container
docker restart restaurant-menu-service

# Remove a container
docker rm restaurant-menu-service
```

### Image Management

```bash
# List images
docker images

# Remove an image
docker rmi restaurant-menu-service

# Build with no cache
docker build --no-cache -t restaurant-menu-service .

# Tag an image
docker tag restaurant-menu-service:latest restaurant-menu-service:v1.0.0
```

### Logs and Debugging

```bash
# View logs
docker logs restaurant-menu-service

# Follow logs
docker logs -f restaurant-menu-service

# Last 100 lines
docker logs --tail 100 restaurant-menu-service

# Execute command in running container
docker exec -it restaurant-menu-service bash

# Inspect container
docker inspect restaurant-menu-service
```

## Environment Variables

Configure the service using environment variables:

| Variable      | Description               | Default                                   |
| ------------- | ------------------------- | ----------------------------------------- |
| `MONGODB_URI` | MongoDB connection string | `mongodb://localhost:27017/restaurant-db` |
| `JAVA_OPTS`   | JVM options               | -                                         |

### Example with Custom JVM Options

```bash
docker run -d \
  --name restaurant-menu-service \
  -p 8085:8085 \
  -e MONGODB_URI=mongodb://mongodb:27017/restaurant-db \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  restaurant-menu-service
```

## Docker Compose Configuration

### Development Setup

```yaml
version: "3.8"

services:
  mongodb:
    image: mongo:latest
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db

  restaurant-menu-service:
    build: .
    ports:
      - "8085:8085"
    environment:
      - MONGODB_URI=mongodb://mongodb:27017/restaurant-db
    depends_on:
      - mongodb

volumes:
  mongodb_data:
```

### Production Setup with Health Checks

```yaml
version: "3.8"

services:
  mongodb:
    image: mongo:latest
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db
    healthcheck:
      test: echo 'db.runCommand("ping").ok' | mongosh localhost:27017/test --quiet
      interval: 10s
      timeout: 5s
      retries: 5
    restart: always

  restaurant-menu-service:
    image: restaurant-menu-service:latest
    build: .
    ports:
      - "8085:8085"
    environment:
      - MONGODB_URI=mongodb://mongodb:27017/restaurant-db
      - JAVA_OPTS=-Xmx1g -Xms512m
    depends_on:
      mongodb:
        condition: service_healthy
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8085/v1/restaurants"]
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  mongodb_data:
```

## Networking

### Docker Compose Network

Docker Compose automatically creates a network for your services. Services can communicate using service names:

```yaml
environment:
  - MONGODB_URI=mongodb://mongodb:27017/restaurant-db
```

### Manual Network Setup

```bash
# Create network
docker network create restaurant-network

# Run MongoDB on network
docker run -d \
  --name mongodb \
  --network restaurant-network \
  mongo:latest

# Run service on network
docker run -d \
  --name restaurant-menu-service \
  --network restaurant-network \
  -p 8085:8085 \
  -e MONGODB_URI=mongodb://mongodb:27017/restaurant-db \
  restaurant-menu-service
```

## Volume Management

### Persistent Data

```bash
# Create named volume
docker volume create mongodb_data

# Use volume
docker run -d \
  --name mongodb \
  -v mongodb_data:/data/db \
  mongo:latest

# List volumes
docker volume ls

# Inspect volume
docker volume inspect mongodb_data

# Remove volume
docker volume rm mongodb_data
```

## Testing the Deployment

### 1. Check Service Health

```bash
curl http://localhost:8085/v1/restaurants
```

### 2. Access Swagger UI

Open in browser:

```
http://localhost:8085/swagger-ui/index.html
```

### 3. Check MongoDB Connection

```bash
docker exec -it mongodb mongosh
> use restaurant-db
> db.restaurants.countDocuments()
> exit
```

## Troubleshooting

### Service Won't Start

```bash
# Check logs
docker logs restaurant-menu-service

# Check if port is in use
lsof -i :8085

# Check container status
docker ps -a
```

### MongoDB Connection Issues

```bash
# Check if MongoDB is running
docker ps | grep mongodb

# Check MongoDB logs
docker logs mongodb

# Test MongoDB connection
docker exec -it mongodb mongosh --eval "db.adminCommand('ping')"
```

### Container Keeps Restarting

```bash
# Check logs for errors
docker logs --tail 50 restaurant-menu-service

# Inspect container
docker inspect restaurant-menu-service

# Check resource usage
docker stats restaurant-menu-service
```

### Clean Slate

```bash
# Stop and remove everything
docker-compose down -v

# Remove all containers
docker rm -f $(docker ps -aq)

# Remove all images
docker rmi -f $(docker images -q)

# Remove all volumes
docker volume prune -f

# Rebuild and start
docker-compose up -d --build
```

## Performance Optimization

### Multi-stage Build (Optional)

Create a more optimized Dockerfile:

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/restaurant-menu-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
CMD ["java", "-jar", "app.jar"]
```

### Resource Limits

```yaml
services:
  restaurant-menu-service:
    image: restaurant-menu-service:latest
    deploy:
      resources:
        limits:
          cpus: "1.0"
          memory: 1G
        reservations:
          cpus: "0.5"
          memory: 512M
```

## Production Considerations

1. **Use specific image tags** instead of `latest`
2. **Set resource limits** to prevent resource exhaustion
3. **Configure health checks** for automatic recovery
4. **Use secrets** for sensitive data (MongoDB credentials)
5. **Enable logging drivers** for centralized logging
6. **Set restart policies** for high availability
7. **Use read-only root filesystem** when possible
8. **Run as non-root user** for security

## Next Steps

- Deploy to Kubernetes (see k8s/ directory)
- Set up CI/CD pipeline
- Configure monitoring and alerting
- Implement backup strategy for MongoDB
