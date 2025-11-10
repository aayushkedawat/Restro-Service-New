# Quick Start Guide

## Prerequisites

- Java 17+
- Maven
- Docker (for MongoDB)

## Setup & Run

### 1. Start MongoDB

```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### 2. Build the Project

```bash
mvn clean package -DskipTests
```

### 3. Run the Service

```bash
java -jar target/restaurant-menu-service-0.0.1-SNAPSHOT.jar
```

The service will start on `http://localhost:8085`

## Access API Documentation

### Swagger UI (Recommended)

Open in your browser:

```
http://localhost:8085/swagger-ui/index.html
```

### Postman

Import the collection file:

```
Restaurant-Menu-Service.postman_collection.json
```

## Quick Test

### Test with curl

```bash
# Get all restaurants
curl http://localhost:8085/v1/restaurants | jq

# Get restaurants in Bengaluru
curl "http://localhost:8085/v1/restaurants?city=Bengaluru" | jq

# Get restaurant menu
curl http://localhost:8085/v1/restaurants/1/menu | jq

# Validate an order
curl -X POST http://localhost:8085/v1/restaurants/validate-order \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": "1",
    "items": [
      {"itemId": "1", "quantity": 2},
      {"itemId": "2", "quantity": 1}
    ]
  }' | jq
```

## Files Generated

1. **openapi.yml** - OpenAPI 3.0 specification
2. **Restaurant-Menu-Service.postman_collection.json** - Postman collection
3. **API_DOCUMENTATION.md** - Complete API documentation

## Initial Data

The service automatically loads initial data on first startup:

- 40 restaurants across multiple cities
- 351 menu items with various categories

Data is loaded from CSV files in `src/main/resources/initial_data/`

## Stopping the Service

```bash
# Stop the Java application (Ctrl+C)

# Stop MongoDB
docker stop mongodb

# To remove MongoDB container
docker rm mongodb
```

## Troubleshooting

### Port 8085 already in use

```bash
# Find and kill the process
lsof -ti:8085 | xargs kill -9
```

### MongoDB connection issues

```bash
# Check if MongoDB is running
docker ps | grep mongodb

# Restart MongoDB
docker restart mongodb
```

## Next Steps

1. Explore the API using Swagger UI
2. Import Postman collection for testing
3. Review the OpenAPI specification
4. Check API_DOCUMENTATION.md for detailed endpoint information
