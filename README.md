# Sales Analytics API

A Spring Boot REST API for managing and analyzing sales data with validation, persistence, and comprehensive error handling.

## Features

- **CRUD Operations** - Create, read, update, and delete sales records
- **Data Validation** - Input validation using Jakarta Bean Validation
- **Persistent Storage** - H2 file-based database with data persistence
- **Error Handling** - Global exception handling with meaningful error responses
- **REST API** - Clean RESTful endpoints with proper HTTP status codes

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA** - Database access layer
- **H2 Database** - Embedded database with file persistence
- **Jakarta Validation** - Bean validation
- **Maven** - Build and dependency management

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Installation

1. Clone the repository
```bash
git clone https://github.com/wovensoul/sales-analytics
cd sales-analytics
```

2. Build the project
```bash
mvn clean compile
```

3. Run the application
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`

### Running Tests
```bash
mvn test
```

### Database Access

The H2 console is available at: `http://localhost:8080/h2-console`

**Connection Details:**
- JDBC URL: `jdbc:h2:file:./data/salesdb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Sales Management

`GET /sales`
Retrieve all sales records.

`POST /sales`
Create a new sale record.

`GET /sales/{id}`
Get a specific sale by ID.

`DELETE /sales/{id}`
Delete a sale record.

### Sample Request

**Create a new sale:**
```bash
curl -X POST http://localhost:8080/sales \
  -H "Content-Type: application/json" \
  -d '{
    "salesperson": "John Doe",
    "country": "USA",
    "product": "Widget A",
    "date": "2024-01-15",
    "amount": 1500.00,
    "boxesShipped": 10
  }'
```

## Data Model

The `Sale` entity includes the following fields:

- `id` (Long) - Auto-generated primary key
- `salesperson` (String) - Name of the salesperson (required)
- `country` (String) - Country where sale occurred (required)
- `product` (String) - Product name (required)
- `date` (LocalDate) - Sale date (required)
- `amount` (Double) - Sale amount in currency (required, >= 0)
- `boxesShipped` (Integer) - Number of boxes shipped (required, >= 0)

## Validation Rules

- All string fields are required and cannot be blank
- `amount` must be zero or positive
- `boxesShipped` must be zero or greater
- `date` cannot be null

## Error Handling

The API provides structured error responses:

- **400 Bad Request** - Validation errors with field-specific messages
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server errors

Example validation error response:
```json
{
  "salesperson": "Salesperson must not be empty",
  "amount": "Amount must be zero or positive"
}
```

## License

This project is licensed under the MIT License.