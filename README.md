# Restaurant Menu Service

A Spring Boot microservice for managing restaurants and menu items in an online food delivery platform. This service provides REST APIs for restaurant operations, menu management, and critical order validation for inter-service communication.

## 🚀 Features

- **Restaurant Management**: CRUD operations for restaurants with filtering by city
- **Menu Management**: Manage menu items with pricing and availability
- **Order Validation**: Critical API for Order Service to validate restaurant status, item availability, and calculate pricing
- **Initial Data Loading**: Automatic CSV data import on first startup
- **API Documentation**: Integrated Swagger UI and OpenAPI specification
- **MongoDB Integration**: NoSQL database for flexible data storage

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (for MongoDB)
- MongoDB 5.0+ (or use Docker)

## 🛠️ Quick Start

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

### 4. Access API Documentation

**Swagger UI (Interactive):**

```
http://localhost:8085/swagger-ui/index.html
```

**OpenAPI Spec:**

- JSON: `http://localhost:8085/v3/api-docs`
- YAML: `http://localhost:8085/v3/api-docs.yaml`

## 📊 Data Model

### Restaurant Entity

| Field     | Type          | Description                                |
| --------- | ------------- | ------------------------------------------ |
| id        | String        | Primary Key                                |
| name      | String        | Restaurant name                            |
| cuisine   | String        | Cuisine type (Mexican, Italian, etc.)      |
| city      | String        | City location (important for delivery)     |
| rating    | Double        | Restaurant rating (0-5)                    |
| isOpen    | Boolean       | Operating status (critical for validation) |
| createdAt | LocalDateTime | Creation timestamp                         |

### Menu Item Entity

| Field        | Type    | Description                                   |
| ------------ | ------- | --------------------------------------------- |
| id           | String  | Primary Key                                   |
| restaurantId | String  | Foreign Key to Restaurant                     |
| name         | String  | Item name                                     |
| category     | String  | Category (Main Course, Starter, etc.)         |
| price        | Double  | Item price                                    |
| isAvailable  | Boolean | Availability status (critical for validation) |

## 🔌 API Endpoints

### Restaurants

| Method | Endpoint                    | Description                                      | Access |
| ------ | --------------------------- | ------------------------------------------------ | ------ |
| GET    | `/v1/restaurants`           | List all restaurants (with optional city filter) | Public |
| GET    | `/v1/restaurants/{id}`      | Get restaurant details                           | Public |
| GET    | `/v1/restaurants/{id}/menu` | Get restaurant menu                              | Public |
| POST   | `/v1/restaurants`           | Create new restaurant                            | Admin  |

### Menu Items

| Method | Endpoint                          | Description                    | Access |
| ------ | --------------------------------- | ------------------------------ | ------ |
| PUT    | `/v1/restaurants/menu-items/{id}` | Update item price/availability | Admin  |

### Order Validation (Critical)

| Method | Endpoint                         | Description                      | Access             |
| ------ | -------------------------------- | -------------------------------- | ------------------ |
| POST   | `/v1/restaurants/validate-order` | Validate order for Order Service | Service-to-Service |

## 🔍 Business Rules

### Order Validation Rules

1. **Restaurant Status**: Orders are rejected if `isOpen = false`
2. **Item Availability**: Orders are rejected if any item has `isAvailable = false`
3. **Pricing Calculation**: Current prices are used to calculate order total
4. **City Information**: Restaurant city is returned for Delivery Service validation

## 📝 API Examples

### Get All Restaurants

```bash
curl http://localhost:8085/v1/restaurants
```

### Filter Restaurants by City

```bash
curl "http://localhost:8085/v1/restaurants?city=Bengaluru"
```

### Get Restaurant Menu

```bash
curl http://localhost:8085/v1/restaurants/1/menu
```

### Validate Order (Critical API)

**Request:**

```bash
curl -X POST http://localhost:8085/v1/restaurants/validate-order \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": "1",
    "items": [
      {"itemId": "1", "quantity": 2},
      {"itemId": "2", "quantity": 1}
    ]
  }'
```

**Success Response (200 OK):**

```json
{
  "isValid": true,
  "restaurantCity": "Pune",
  "calculatedItemsTotal": 635.68,
  "validatedItems": [
    { "itemId": "1", "price": 240.86, "quantity": 2 },
    { "itemId": "2", "price": 153.96, "quantity": 1 }
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

## 📦 Initial Data

The service comes pre-loaded with sample data:

- **40 Restaurants** across multiple cities (Bengaluru, Mumbai, Delhi, Pune, Chennai, Kolkata, Hyderabad, Ahmedabad)
- **351 Menu Items** with various categories and prices

Data is automatically loaded from CSV files in `src/main/resources/initial_data/` on first startup.

## 🧪 Testing

### Using Swagger UI

1. Navigate to `http://localhost:8085/swagger-ui/index.html`
2. Explore and test all endpoints interactively

### Using Postman

Import the collection: `Restaurant-Menu-Service.postman_collection.json`

The collection includes:

- Get all restaurants
- Filter by city
- Get restaurant details
- Get menu
- Create restaurant
- Update menu item
- Validate order scenarios (success, closed restaurant, unavailable item)

### Using cURL

See examples in [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## 🐳 Docker Support

### Build Docker Image

```bash
mvn clean package -DskipTests
docker build -t restaurant-menu-service .
```

### Run with Docker

```bash
docker run -p 8085:8085 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017/restaurant-db \
  restaurant-menu-service
```

### Using Kubernetes

```bash
kubectl apply -f k8s/
```

## ⚙️ Configuration

### Environment Variables

| Variable      | Description               | Default                                   |
| ------------- | ------------------------- | ----------------------------------------- |
| `MONGODB_URI` | MongoDB connection string | `mongodb://localhost:27017/restaurant-db` |
| `server.port` | Application port          | `8085`                                    |

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
server.port=8085
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/restaurant-db}
```

## 📚 Documentation Files

- **README.md** - This file (project overview)
- **API_DOCUMENTATION.md** - Complete API reference
- **QUICK_START.md** - Quick setup guide
- **openapi.yml** - OpenAPI 3.0 specification
- **Restaurant-Menu-Service.postman_collection.json** - Postman collection

## 🏗️ Project Structure

```
restaurant-menu-service/
├── src/
│   ├── main/
│   │   ├── java/com/fooddelivery/restaurantmenuservice/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── model/           # Domain models
│   │   │   ├── repository/      # MongoDB repositories
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       ├── initial_data/    # CSV files for initial data
│   │       └── application.properties
├── k8s/                         # Kubernetes manifests
├── openapi.yml                  # OpenAPI specification
├── Restaurant-Menu-Service.postman_collection.json
└── pom.xml
```

## 🔧 Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MongoDB
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven
- **Containerization**: Docker

## 🚦 Health Check

Check if the service is running:

```bash
curl http://localhost:8085/v1/restaurants
```

## 🛑 Stopping the Service

```bash
# Stop the application (Ctrl+C)

# Stop MongoDB
docker stop mongodb

# Remove MongoDB container (optional)
docker rm mongodb
```

## 🐛 Troubleshooting

### Port 8085 already in use

```bash
lsof -ti:8085 | xargs kill -9
```

### MongoDB connection issues

```bash
# Check if MongoDB is running
docker ps | grep mongodb

# Restart MongoDB
docker restart mongodb
```

### Data not loading

Delete the MongoDB database and restart the service to reload initial data:

```bash
docker exec -it mongodb mongosh
> use restaurant-db
> db.dropDatabase()
> exit
```

## 📄 License

This project is part of the Food Delivery Platform microservices architecture.

## 👥 Contact

For issues or questions, contact: support@fooddelivery.com

## 🔗 Related Services

- Order Service (consumes validate-order API)
- Delivery Service (uses restaurant city information)
- Client Applications (consume public APIs)
