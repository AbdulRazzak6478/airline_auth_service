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
