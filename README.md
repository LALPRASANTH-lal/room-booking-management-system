# Room Booking Management System

A backend application developed using Java Spring Boot that allows users to register, log in securely, view available rooms, and manage room bookings.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Swagger / OpenAPI
- Postman

## Key Features

- User registration and login
- Secure password encryption using BCrypt
- JWT-based authentication
- Role-based authorization (USER and ADMIN)
- Room management for administrators
- Room listing for users
- Room booking and cancellation
- Booking conflict and availability validation
- User-specific booking history
- Global exception handling
- REST API documentation using Swagger

## Project Architecture

Controller → Service → Repository → MySQL Database

- **Controller:** Handles incoming REST API requests.
- **Service:** Contains business logic and validation.
- **Repository:** Performs database operations using JPA.
- **Entity:** Represents database tables.
- **Security:** Handles JWT authentication and role-based access.
- **DTO:** Transfers request and response data.

## API Documentation

After starting the application, open:

http://localhost:8080/swagger-ui/index.html

## Running the Project

1. Clone this repository.
2. Create a MySQL database for the application.
3. Configure database credentials in `application.properties`.
4. Run the application using your IDE or Maven:

```bash
mvn spring-boot:run
```

5. Access the application at:

http://localhost:8080

## Security

- Passwords are stored using BCrypt hashing.
- JWT tokens protect secured endpoints.
- ADMIN and USER roles have different permissions.
- Users should only access and manage their own bookings.

## Developer

Lal Prasanth B.

Java Full Stack Developer | Spring Boot | React | MySQL