# Airline Auth Service

**Microservice #3** - Authentication & Authorization Service for Airline Management System

## 📋 Overview

The Airline Auth Service is a robust, production-ready authentication and authorization microservice built with **Spring Boot 3.5.4** and **Java 21**. This is the third microservice in the airline management ecosystem, handling all user authentication, JWT token generation, and secure access control.

## 🎯 Purpose

This microservice is responsible for:
- User registration and login management
- JWT token generation and validation
- Spring Security integration for secure API endpoints
- User profile management
- Role-based access control (RBAC)
- Password management and validation

## 🏗️ Architecture

### Technology Stack
- **Framework**: Spring Boot 3.5.4
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT (JJWT 0.12.6)
- **Build Tool**: Maven
- **Database Migrations**: Flyway
- **ORM**: Spring Data JPA

## ✨ Key Features

### 1. **User Authentication**
- Secure user registration with validation
- Login with email/username and password
- JWT token-based authentication
- Token refresh mechanism

### 2. **Security**
- Spring Security integration
- Password encryption using BCrypt
- JWT token generation and validation
- Role-based access control

### 3. **User Management**
- User profile creation and updates
- User information retrieval
- Role assignment and management

### 4. **Database Management**
- PostgreSQL for persistent data storage
- Flyway for database version control and migrations
- Spring Data JPA for simplified database operations

### Project Structure
```

airline_auth_service/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
├── .gitignore
├── .gitattributes
│
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.jar
│       ├── maven-wrapper.properties
│       └── MavenWrapperDownloader.java
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── airline/
    │   │           └── auth/
    │   │               ├── AuthApplication.java
    │   │               │   (Spring Boot entry point)
    │   │               │
    │   │               ├── config/
    │   │               │   ├── SecurityConfig.java
    │   │               │   │   (Spring Security configuration)
    │   │               │   │   - Configures security filters
    │   │               │   │   - JWT authentication filter
    │   │               │   │   - Exception handlers for auth
    │   │               │   │   - Password encoder
    │   │               │   │
    │   │               │   ├── JwtAuthenticationFilter.java
    │   │               │   │   (JWT authentication filter)
    │   │               │   │   - Intercepts requests
    │   │               │   │   - Validates JWT tokens
    │   │               │   │   - Sets authentication in context
    │   │               │   │   - Handles token validation errors
    │   │               │   │
    │   │               │   ├── JwtAuthenticationEntryPoint.java
    │   │               │   │   (Handles 401 Unauthorized)
    │   │               │   │   - Returns formatted error response
    │   │               │   │   - Called when auth fails
    │   │               │   │
    │   │               │   ├── JwtAccessDeniedHandler.java
    │   │               │   │   (Handles 403 Forbidden)
    │   │               │   │   - Returns formatted error response
    │   │               │   │   - Called when user lacks permissions
    │   │               │   │
    │   │               │   ├── CustomUserDetails.java
    │   │               │   │   (Custom Spring Security UserDetails)
    │   │               │   │   - Wraps User entity
    │   │               │   │   - Provides user authorities/roles
    │   │               │   │   - Used in authentication context
    │   │               │   │
    │   │               │   ├── CustomUserDetailsService.java
    │   │               │   │   (Custom UserDetailsService)
    │   │               │   │   - Loads user by username/email
    │   │               │   │   - Creates CustomUserDetails
    │   │               │   │   - Implements UserDetailsService interface
    │   │               │   │
    │   │               │   └── StartupListener.java
    │   │               │       (Application startup banner)
    │   │               │
    │   │               ├── constants/
    │   │               │   └── ApiRoutes.java
    │   │               │       (API endpoint routes constants)
    │   │               │       - /auth/login
    │   │               │       - /auth/register
    │   │               │       - /auth/refresh-token
    │   │               │       - /users/*
    │   │               │
    │   │               ├── controllers/
    │   │               │   ├── HomeController.java
    │   │               │   │   (Home/Health check endpoint)
    │   │               │   │   - GET / (public)
    │   │               │   │
    │   │               │   ├── AuthController.java
    │   │               │   │   (Authentication endpoints)
    │   │               │   │   - POST /auth/login (authenticate user)
    │   │               │   │   - POST /auth/register (create new user)
    │   │               │   │   - POST /auth/refresh-token (get new access token)
    │   │               │   │
    │   │               │   └── UserController.java
    │   │               │       (User management endpoints)
    │   │               │       - GET /users (list all users - admin)
    │   │               │       - GET /users/{id} (get user by ID)
    │   │               │       - PUT /users/{id} (update user - admin)
    │   │               │       - DELETE /users/{id} (delete user - admin)
    │   │               │
    │   │               ├── dto/
    │   │               │   ├── common/
    │   │               │   │   ├── ApiResponse.java
    │   │               │   │   │   (Generic API response wrapper)
    │   │               │   │   │   - success: Boolean
    │   │               │   │   │   - status: int (HTTP status code)
    │   │               │   │   │   - message: String
    │   │               │   │   │   - data: T (generic payload)
    │   │               │   │   │   - errors: List<String>
    │   │               │   │   │   - timestamp: LocalDateTime
    │   │               │   │   │
    │   │               │   │   ├── ApiResponseBuilder.java
    │   │               │   │   │   (Builder pattern for API responses)
    │   │               │   │   │
    │   │               │   │   └── PageResponse.java
    │   │               │   │       (Pagination response wrapper)
    │   │               │   │       - content: List<T>
    │   │               │   │       - pageNumber: int
    │   │               │   │       - pageSize: int
    │   │               │   │       - totalElements: long
    │   │               │   │       - totalPages: int
    │   │               │   │       - isFirst: boolean
    │   │               │   │       - isLast: boolean
    │   │               │   │
    │   │               │   ├── request/
    │   │               │   │   ├── LoginUserRequest.java
    │   │               │   │   │   (Login request)
    │   │               │   │   │   - email: String
    │   │               │   │   │   - password: String
    │   │               │   │   │   - @NotNull, @NotBlank validations
    │   │               │   │   │
    │   │               │   │   ├── RegisterUserRequest.java
    │   │               │   │   │   (Registration request)
    │   │               │   │   │   - firstName: String
    │   │               │   │   │   - lastName: String
    │   │               │   │   │   - email: String (unique)
    │   │               │   │   │   - password: String
    │   │               │   │   │   - role: Role (CUSTOMER, ADMIN, etc.)
    │   │               │   │   │   - Email/password validations
    │   │               │   │   │
    │   │               │   │   ├── RefreshTokenRequest.java
    │   │               │   │   │   (Refresh token request)
    │   │               │   │   │   - refreshToken: String
    │   │               │   │   │
    │   │               │   │   └── UserSearchListRequest.java
    │   │               │   │       (User search/filter request)
    │   │               │   │       - Search criteria (pagination)
    │   │               │   │
    │   │               │   └── response/
    │   │               │       ├── LoginTokenResponse.java
    │   │               │       │   (Login success response)
    │   │               │       │   - accessToken: String (JWT)
    │   │               │       │   - refreshToken: String
    │   │               │       │   - expiresIn: long
    │   │               │       │
    │   │               │       └── UserResponse.java
    │   │               │           (User data response)
    │   │               │           - id: UUID
    │   │               │           - firstName: String
    │   │               │           - lastName: String
    │   │               │           - email: String
    │   │               │           - role: Role
    │   │               │           - userStatus: UserStatus
    │   │               │
    │   │               ├── entity/
    │   │               │   ├── User.java
    │   │               │   │   (JPA Entity - User)
    │   │               │   │   - id: UUID (primary key)
    │   │               │   │   - firstName: String
    │   │               │   │   - lastName: String
    │   │               │   │   - email: String (unique)
    │   │               │   │   - password: String (hashed)
    │   │               │   │   - role: Role enum (CUSTOMER, ADMIN, AIRLINE_MANAGER)
    │   │               │   │   - userStatus: UserStatus enum (ACTIVE, INACTIVE)
    │   │               │   │   - createdAt: LocalDateTime
    │   │               │   │   - updatedAt: LocalDateTime
    │   │               │   │
    │   │               │   └── RefreshToken.java
    │   │               │       (JPA Entity - Refresh Token)
    │   │               │       - id: UUID
    │   │               │       - token: String (unique)
    │   │               │       - userId: UUID (foreign key to User)
    │   │               │       - expiryDate: LocalDateTime
    │   │               │       - createdAt: LocalDateTime
    │   │               │
    │   │               ├── enums/
    │   │               │   ├── Role.java
    │   │               │   │   (User roles enum)
    │   │               │   │   - CUSTOMER
    │   │               │   │   - ADMIN
    │   │               │   │   - AIRLINE_MANAGER
    │   │               │   │   - SUPER_ADMIN
    │   │               │   │
    │   │               │   └── UserStatus.java
    │   │               │       (User status enum)
    │   │               │       - ACTIVE
    │   │               │       - INACTIVE
    │   │               │
    │   │               ├── exception/
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   │   (Centralized exception handling)
    │   │               │   │   - Handles all exceptions globally
    │   │               │   │   - Returns formatted error responses
    │   │               │   │   - Handles validation errors
    │   │               │   │   - Handles custom exceptions
    │   │               │   │
    │   │               │   ├── ResourceNotFoundException.java
    │   │               │   │   (Thrown when user/token not found)
    │   │               │   │
    │   │               │   └── DuplicateResourceException.java
    │   │               │       (Thrown when email already exists)
    │   │               │
    │   │               ├── mapper/
    │   │               │   └── UserMapper.java
    │   │               │       (Entity ↔ DTO mapping)
    │   │               │       - toResponse(User): UserResponse
    │   │               │       - toEntity(RegisterUserRequest): User
    │   │               │       - Handles role and status mapping
    │   │               │
    │   │               ├── repositories/
    │   │               │   ├── UserRepository.java
    │   │               │   │   (Spring Data JPA Repository)
    │   │               │   │   - findByEmail(String): Optional<User>
    │   │               │   │   - findByRole(Role): List<User>
    │   │               │   │   - findByUserStatus(UserStatus): List<User>
    │   │               │   │
    │   │               │   └── RefreshTokenRepository.java
    │   │               │       (Spring Data JPA Repository)
    │   │               │       - findByToken(String): Optional<RefreshToken>
    │   │               │       - findByUserId(UUID): Optional<RefreshToken>
    │   │               │       - deleteByUserId(UUID): void
    │   │               │
    │   │               ├── services/
    │   │               │   ├── AuthService.java
    │   │               │   │   (Interface - Authentication service)
    │   │               │   │   - login(LoginUserRequest): LoginTokenResponse
    │   │               │   │   - register(RegisterUserRequest): UserResponse
    │   │               │   │   - refreshToken(RefreshTokenRequest): LoginTokenResponse
    │   │               │   │
    │   │               │   ├── UserService.java
    │   │               │   │   (Interface - User management service)
    │   │               │   │   - getAllUsers(): List<UserResponse>
    │   │               │   │   - getUserById(UUID): UserResponse
    │   │               │   │   - updateUser(UUID, Request): UserResponse
    │   │               │   │   - deleteUser(UUID): void
    │   │               │   │
    │   │               │   ├── JwtService.java
    │   │               │   │   (JWT token handling service)
    │   │               │   │   - generateAccessToken(User): String
    │   │               │   │   - generateRefreshToken(User): String
    │   │               │   │   - validateToken(String): boolean
    │   │               │   │   - getEmailFromToken(String): String
    │   │               │   │   - getExpirationTime(String): Date
    │   │               │   │
    │   │               │   └── implementation/
    │   │               │       ├── AuthServiceImpl.java
    │   │               │       │   (Authentication logic)
    │   │               │       │   - Validates credentials
    │   │               │       │   - Creates user on registration
    │   │               │       │   - Generates JWT tokens
    │   │               │       │   - Checks for duplicate emails
    │   │               │       │   - Manages refresh tokens
    │   │               │       │
    │   │               │       └── UserServiceImpl.java
    │   │               │           (User management logic)
    │   │               │           - Retrieves users from DB
    │   │               │           - Updates user information
    │   │               │           - Deletes users
    │   │               │           - Applies role-based filtering
    │   │
    │   └── resources/
    │       ├── application.properties
    │       │   (Application configuration)
    │       │   - spring.application.name=auth-service
    │       │   - server.port=8081
    │       │   - Database configuration
    │       │   - JWT secret and expiration
    │       │   - Flyway migration settings
    │       │
    │       └── db/
    │           └── migration/
    │               ├── V1__create_user_table.sql
    │               │   (Create USER table)
    │               │   Columns: id, first_name, last_name, email (unique),
    │               │            password (hashed), role, user_status,
    │               │            created_at, updated_at
    │               │
    │               └── V2__create_referesh_token_table.sql
    │                   (Create REFRESH_TOKEN table)
    │                   Columns: id, token (unique), user_id (FK),
    │                            expiry_date, created_at
    │
    └── test/
        └── java/
            └── com/
                └── airline/
                    └── auth/
                        └── AuthApplicationTests.java
                            (Spring Boot integration tests)


```
