# ChillCinema - Movie Ticket Booking System

A comprehensive online movie ticket booking and management system built with Spring Boot, providing a seamless experience for customers to browse movies, book tickets, and manage their reservations.

## 🎬 Overview

ChillCinema is a full-featured cinema management platform that enables users to:
- Browse current and upcoming movies
- View showtimes and theater information
- Select and book seats in real-time
- Make secure online payments
- Manage bookings and receive email confirmations

## 🚀 Technologies

### Backend
- **Framework**: Spring Boot 3.5.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: Microsoft SQL Server
- **Security**: JWT (JSON Web Token) Authentication
- **Payment Gateway**: PayOS Integration
- **Email Service**: Gmail SMTP
- **Scheduling**: Spring Scheduler for automated tasks

### Key Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Mail
- JJWT (0.12.6) for JWT handling
- SQL Server JDBC Driver
- Lombok for reducing boilerplate code
- MapStruct for object mapping
- Springdoc OpenAPI (Swagger UI)

## 📋 Features

### 1. Movie Management
- Create, read, update, and delete movies
- Search movies by name or status (NOW_SHOWING, COMING_SOON, etc.)
- Manage movie banners and details
- Upload movie posters and media

### 2. Booking System
- Real-time seat selection
- Multiple seat booking in a single transaction
- Booking status tracking (PENDING, CONFIRMED, CANCELLED)
- Automatic booking cleanup for expired reservations
- Booking history for users

### 3. Theater & Showtime Management
- Manage multiple theaters
- Configure showtimes for movies
- Seat management with different types (VIP, Regular, etc.)
- Seat availability tracking (AVAILABLE, BOOKED, SELECTED, MAINTENANCE)

### 4. Payment Integration
- Secure payment processing via PayOS
- Multiple payment status handling (PENDING, PAID, CANCELLED, FAILED)
- Webhook support for payment confirmation
- Payment history and receipts

### 5. Promotion System
- Apply discount codes to bookings
- Manage promotional campaigns
- Automatic price calculation with discounts

### 6. User Management
- User registration and authentication
- Role-based access control (USER, ADMIN, MANAGER)
- JWT-based secure authentication
- Profile management

### 7. Reports & Statistics
- User feedback and complaint system
- Report status tracking (PENDING, PROCESSING, RESOLVED, REJECTED)
- Revenue statistics
- Booking analytics

### 8. Email Notifications
- Booking confirmation emails
- Payment receipt notifications
- Promotional emails
- System notifications

## 🛠️ Installation & Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- SQL Server database
- Gmail account (for email service)
- PayOS account (for payment gateway)

### Configuration

1. Clone the repository:
```bash
git clone <repository-url>
cd MSMBO_BE
```

2. Configure database connection in `application.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://your-server:1433;databaseName=your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
```

3. Set up JWT secret key:
```properties
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

4. Configure email service:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

5. Set up PayOS credentials:
```properties
payos.client-id=your-client-id
payos.api-key=your-api-key
payos.checksum-key=your-checksum-key
```

### Running the Application

Using Maven:
```bash
./mvnw spring-boot:run
```

Or build and run the JAR:
```bash
./mvnw clean package
java -jar target/ticket-booking-system-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

### Main API Endpoints

#### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

#### Movies
- `GET /api/movie` - Get all movies
- `GET /api/movie/{name}` - Search movies by name
- `GET /api/movie/status/{status}` - Get movies by status
- `POST /api/movie` - Create new movie (Admin)
- `PUT /api/movie/{id}` - Update movie (Admin)
- `DELETE /api/movie/{id}` - Delete movie (Admin)

#### Showtimes
- `GET /api/showtime` - Get all showtimes
- `GET /api/showtime/{id}` - Get showtime details
- `POST /api/showtime` - Create showtime (Admin)
- `PUT /api/showtime/{id}` - Update showtime (Admin)
- `DELETE /api/showtime/{id}` - Delete showtime (Admin)

#### Bookings
- `GET /api/booking` - Get user bookings
- `POST /api/booking` - Create new booking
- `PUT /api/booking/{id}` - Update booking
- `DELETE /api/booking/{id}` - Cancel booking

#### Seats
- `GET /api/seat/showtime/{showtimeId}` - Get available seats for showtime
- `POST /api/seat/hold` - Hold seats temporarily
- `POST /api/seat/release` - Release held seats

#### Payments
- `POST /api/payment/create` - Create payment
- `POST /api/payment/payos_transfer_handler` - PayOS webhook
- `GET /api/payment/history` - Get payment history

#### Promotions
- `GET /api/promotion` - Get active promotions
- `POST /api/promotion/apply` - Apply promotion code
- `POST /api/promotion` - Create promotion (Admin)

#### Reports
- `POST /api/report` - Submit report
- `GET /api/report` - Get all reports (Admin)
- `PUT /api/report/{id}/status` - Update report status (Admin)

#### Statistics
- `GET /api/statistics/revenue` - Get revenue statistics (Admin)
- `GET /api/statistics/bookings` - Get booking statistics (Admin)

## 🔐 Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control (RBAC)
- CORS configuration for frontend integration
- Secure payment processing

## 🌐 Deployment

### Docker Deployment

A Dockerfile is included for containerized deployment:

```bash
docker build -t chillcinema-backend .
docker run -p 8080:8080 chillcinema-backend
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/ticket_booking_system/
│   │   ├── auth/              # Security & JWT configuration
│   │   ├── configuration/     # Application configuration
│   │   ├── controller/        # REST API endpoints
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── entity/            # JPA entities
│   │   ├── Enum/              # Enumerations
│   │   ├── exception/         # Exception handling
│   │   ├── mapper/            # Object mappers
│   │   ├── repository/        # Data access layer
│   │   └── service/           # Business logic
│   └── resources/
│       └── application.properties  # Configuration file
└── test/                      # Unit and integration tests
```

## 🔧 Configuration Properties

### Application Settings
- Base URL configuration for API and frontend
- Database connection pooling
- JPA/Hibernate settings
- Email service configuration

### JWT Settings
- Token expiration time
- Secret key for signing tokens

### Payment Gateway Settings
- PayOS integration credentials
- Return, webhook, and cancel URLs

## 🧪 Testing

Run tests using Maven:
```bash
./mvnw test
```

## 📝 Database Schema

The system uses the following main entities:
- **User**: User accounts and authentication
- **Movie**: Movie information and metadata
- **Theater**: Cinema theater details
- **Showtime**: Movie screening schedules
- **Seat**: Seat configuration and availability
- **SeatType**: Seat categories (VIP, Regular, etc.)
- **Booking**: Ticket reservations
- **BookingDetail**: Individual seat bookings
- **Payment**: Payment transactions
- **Promotion**: Discount and promotional codes
- **Report**: User feedback and complaints

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is part of the MSMBO Project initiative.

## 📧 Contact

For any inquiries or support, please contact the development team.

---

**Note**: This is a production-ready application. Make sure to properly configure all environment variables and security settings before deployment.
