# Restaurant Menu Service - API Documentation

## Overview

Restaurant and Menu management microservice for Online Food Delivery Platform.

## Base URL

- **Local Development**: `http://localhost:8085`
- **Production**: `https://api.fooddelivery.com`

## API Documentation Access

### Swagger UI (Interactive)

Access the interactive API documentation at:

```
http://localhost:8085/swagger-ui/index.html
```

### OpenAPI Specification

- **JSON Format**: `http://localhost:8085/v3/api-docs`
- **YAML Format**: `http://localhost:8085/v3/api-docs.yaml`
- **Static File**: `openapi.yml` (in project root)

### Postman Collection

Import the Postman collection from:

```
Restaurant-Menu-Service.postman_collection.json
```

## Quick Start

### 1. Start the Service

```bash
# Start MongoDB
docker start mongodb

# Run the service
java -jar target/restaurant-menu-service-0.0.1-SNAPSHOT.jar
```

### 2. Access Swagger UI

Open your browser and navigate to:

```
http://localhost:8085/swagger-ui/index.html
```

### 3. Import Postman Collection

1. Open Postman
2. Click "Import"
3. Select `Restaurant-Menu-Service.postman_collection.json`
4. The collection includes all endpoints with example requests

## API Endpoints

### Restaurants

#### Get All Restaurants

```http
GET /v1/restaurants
```

Query Parameters:

- `city` (optional): Filter by city name
- `page` (optional): Page number (default: 0)
- `size` (optional): Items per page (default: 20)

Example:

```bash
curl http://localhost:8085/v1/restaurants?city=Bengaluru
```

#### Get Restaurant by ID

```http
GET /v1/restaurants/{id}
```

Example:

```bash
curl http://localhost:8085/v1/restaurants/1
```

#### Get Restaurant Menu

```http
GET /v1/restaurants/{id}/menu
```

Example:

```bash
curl http://localhost:8085/v1/restaurants/1/menu
```

#### Create Restaurant (Admin)

```http
POST /v1/restaurants
Content-Type: application/json

{
  "name": "New Restaurant",
  "cuisine": "Italian",
  "city": "Mumbai",
  "rating": 4.5,
  "isOpen": true
}
```

### Menu Items

#### Update Menu Item (Admin)

```http
PUT /v1/restaurants/menu-items/{id}
Content-Type: application/json

{
  "price": 250.00,
  "available": true
}
```

### Order Validation

#### Validate Order (Critical API for Order Service)

```http
POST /v1/restaurants/validate-order
Content-Type: application/json

{
  "restaurantId": "1",
  "items": [
    {
      "itemId": "1",
      "quantity": 2
    },
    {
      "itemId": "2",
      "quantity": 1
    }
  ]
}
```

**Success Response (200 OK):**

```json
{
  "isValid": true,
  "restaurantCity": "Pune",
  "calculatedItemsTotal": 635.68,
  "validatedItems": [
    {
      "itemId": "1",
      "price": 240.86,
      "quantity": 2
    },
    {
      "itemId": "2",
      "price": 153.96,
      "quantity": 1
    }
  ]
}
```

**Failure Response (409 Conflict):**

```json
{
  "isValid": false,
  "rejectionReason": "Restaurant is currently closed and cannot accept orders."
}
```

## Business Rules

### Order Validation Rules

1. **Restaurant Status**: Orders are rejected if `isOpen = false`
2. **Item Availability**: Orders are rejected if any item has `isAvailable = false`
3. **Pricing**: Current prices are returned for Order Service to calculate totals
4. **City Information**: Restaurant city is returned for Delivery Service validation

## Testing with Postman

The Postman collection includes test scenarios:

1. **Get All Restaurants** - List all restaurants
2. **Get Restaurants by City** - Filter by Bengaluru
3. **Get Restaurant by ID** - Get details for restaurant #1
4. **Get Restaurant Menu** - Get menu for restaurant #1
5. **Create Restaurant** - Admin endpoint to add new restaurant
6. **Update Menu Item** - Admin endpoint to update price/availability
7. **Validate Order - Success** - Valid order from open restaurant
8. **Validate Order - Restaurant Closed** - Should return 409
9. **Validate Order - Item Unavailable** - Should return 409

## Environment Variables

Set the `baseUrl` variable in Postman:

- **Local**: `http://localhost:8085`
- **Production**: `https://api.fooddelivery.com`

## Sample Data

The service comes pre-loaded with:

- **40 Restaurants** across multiple cities (Bengaluru, Mumbai, Delhi, Pune, Chennai, etc.)
- **351 Menu Items** with various categories (Main Course, Starter, Dessert, Beverage)

## Response Codes

- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `404 Not Found` - Resource not found
- `409 Conflict` - Business rule validation failed

## Support

For issues or questions, contact: support@fooddelivery.com
