# E-Scooter Rental System

## Project Overview
This is a web application for electric scooter rental in urban areas, providing a user-friendly interface and comprehensive rental management functions.

## Key Features

- **User Management System**: User registration, login, password recovery, with verification code security
- **Scooter Booking System**: Multiple rental options (hourly, 4-hour, daily, weekly)
- **Multiple Payment Methods**: Supports Alipay and bank card payments
- **Diverse Discount Rules**: Special discounts for students, seniors, and frequent customers
- **Complete Order Management**: Creation, cancellation, extension, and return of scooters
- **Deposit Management System**: Refund deposit based on scooter battery level upon return
- **Revenue Statistics & Analysis**: Weekly and daily revenue reports with visual chart analysis
- **User Feedback System**: Collect and process user feedback

## System Architecture

```
.
├── Frontend (Vue.js)    # User interface, handling user interactions
└── Backend (Spring Boot) # Business logic, data processing and storage
```

- Frontend and backend communicate via RESTful APIs
- JWT used for user authentication
- Data stored in MySQL database

## Technology Stack

### Frontend
- Vue.js 3
- Vite
- Vue Router 4
- Vuex
- Axios
- Element Plus UI
- ECharts (Data visualization)

### Backend
- Spring Boot 3.2.2
- Spring Data JPA
- Spring Security
- MySQL 8.0
- JWT Authentication
- Alipay SDK Integration
- Spring Mail (Email notifications)

## Online Demo

The application is deployed and accessible at: http://118.24.22.77

### Default Accounts

#### Admin Account
- Username: admin
- Password: Password123

#### Regular User Account
- Username: user
- Password: Password123

## Deployment Notes

### CORS Configuration
For deployment to a production environment, CORS settings need to be updated to allow requests from the production domain:

1. Update `SecurityConfig.java` to include production domain in allowed origins
2. Update `WebConfig.java` to include production domain in allowed origins
3. Update all controller `@CrossOrigin` annotations to include production domain

The current configuration already includes support for the demo server at http://118.24.22.77.

## Installation & Configuration

### System Requirements
- Node.js 16+
- JDK 17
- Maven 3.9+
- MySQL 8.0+

### Backend Setup

1. Clone the repository

```bash
git clone [repository-url]
cd [project-folder]
```

2. Configure database

```sql
CREATE DATABASE IF NOT EXISTS vuesb;
```

3. Modify configuration file (backend/src/main/resources/application.properties)

```properties
# Database connection
# 数据库连接
spring.datasource.url=jdbc:mysql://localhost:3306/vuesb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your-database-password

# Email configuration (if used)
# 邮箱配置（如果使用）
spring.mail.host=smtp.your-email-provider.com
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password

# Alipay configuration (if used)
# 支付宝配置（如果使用）
alipay.appId=your-alipay-AppID
alipay.appPrivateKey=your-alipay-private-key
alipay.alipayPublicKey=alipay-public-key
```

4. Build and run the backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend service will run on http://localhost:8080

### Frontend Setup

1. Install dependencies

```bash
cd frontend
npm install
```

2. Run the development server

```bash
npm run dev
```

The frontend service will run on http://localhost:5173

## Database Setup

### Database Tables
The system uses the following main tables:
- `b_user`: User information
- `b_scooter`: Scooter information
- `b_order`: Order information
- `b_store`: Store information
- `b_discount_rule`: Discount rules
- `b_feedback`: User feedback
- `b_weekly_revenue`: Weekly revenue statistics
- `b_daily_revenue`: Daily revenue statistics

Initial data will be automatically loaded through `schema.sql` and `data.sql`.

## User Guide

### Regular User Features
- **Account Management**: Register, login, edit personal information, recover password
- **Browse Scooters**: View available scooters and pricing information
- **Book Scooters**: Select time periods and rental types for booking
- **Pay Orders**: Complete payment via Alipay or bank card
- **Order Management**: View, cancel, extend personal orders
- **Return Scooters**: Return after use and receive deposit refund
- **Submit Feedback**: Submit questions or suggestions to the system

### Admin Features
- **Scooter Management**: Add, edit, delete scooter information
- **User Management**: View and manage system users
- **Order Management**: View all orders and handle special cases
- **Revenue Analysis**: View revenue statistics reports and analysis charts
- **Process Feedback**: Reply to and process user-submitted feedback

## Developer Guide

### Code Structure
- `frontend/src/components/`: Frontend UI components
- `frontend/src/views/`: Page views
- `frontend/src/router/`: Route configuration
- `frontend/src/store/`: Vuex state management
- `backend/src/main/java/group6/demo/controller/`: API controllers
- `backend/src/main/java/group6/demo/service/`: Business logic services
- `backend/src/main/java/group6/demo/entity/`: Data entities
- `backend/src/main/java/group6/demo/repository/`: Data access layer

### API Documentation
API documentation can be accessed via Swagger UI after starting the backend service:
http://localhost:8080/swagger-ui.html

## Frequently Asked Questions

**Q: How to enable test mode?**
A: Set `app.test-mode=true` in the application.properties file

**Q: How to reset the database?**
A: Delete and recreate the database, or modify `spring.jpa.hibernate.ddl-auto=create` to recreate the table structure

**Q: How to configure the Alipay sandbox environment?**
A: Create a sandbox application on the Alipay Open Platform, obtain the corresponding AppID and keys, and configure them in the application.properties file

## Contribution Guidelines
Pull Requests and Issues are welcome to improve this project. Before submitting code, please ensure:
1. Code has been tested
2. Follow the project's code style
3. Update relevant documentation

## License
[Add project license information]