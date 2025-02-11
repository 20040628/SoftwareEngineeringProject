# SoftwareEngineeringProject
Develop an application for hiring electric scooters in the City Centre

## Project Structure

```
.
├── frontend/    # Vue.js frontend project
└── backend/     # Spring Boot backend project
```

## Tech Stack

### Frontend
- Vue.js 3
- Vite
- Vue Router
- Axios

### Backend
- Spring Boot 3.2.2
- Spring Data JPA
- MySQL 8.0
- Maven

## Development Requirements

- Node.js 16+
- JDK 17
- Maven 3.9+
- MySQL 8.0+

## Database Setup

1. Install MySQL 8.0+
2. Create database (if not exists, system will create automatically):
```sql
CREATE DATABASE IF NOT EXISTS vuesb;
```
3. Configure database connection (in `application.properties`):
   - URL: jdbc:mysql://localhost:3306/vuesb
   - Username: root
   - Password: root

## Running the Project

### Backend

```bash
cd backend
mvn spring-boot:run
```

Backend service will run on http://localhost:8080

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend service will run on http://localhost:5173

## API Documentation

### User Related Interfaces

- GET /api/users - Get All Users
- GET /api/users/{id} - Get User by ID
- POST /api/users - Create New User

## Database

Project uses MySQL database. You can access the database using:

1. MySQL Command Line:
```bash
mysql -u root -p
```

2. MySQL Workbench or other GUI tools with these credentials:
- Host: localhost
- Port: 3306
- Database: vuesb
- Username: root
- Password: root