# 💈 Beauty Salon (Male) Management System – Appointment Management

A web-based appointment management module for a men's grooming salon, built with **Spring Boot**, **Thymeleaf**, **Spring Security** and **MySQL**. Customers can register, book and manage their appointments, while admins can manage every booking and customer from a dashboard.

---

## ✨ Features

### 👤 Customer
- Register and log in (BCrypt-hashed passwords)
- Book an appointment (choose service, package, stylist, date and time)
- View all your own appointments
- Reschedule an appointment (subject to the package's cutoff time)
- Cancel an appointment
- View and update your profile

### 🛠️ Admin
- Dashboard with customer management (view, update, delete)
- View all appointments, filter by **status** and/or **date**
- Update appointment status (Confirm / Complete / No-Show / Cancel)
- Permanently delete appointment records
- **File Records** page – shows the appointment data stored in a text file

---

## 🧱 Tech Stack

| Layer      | Technology                                   |
|------------|----------------------------------------------|
| Backend    | Java, Spring Boot, Spring MVC                |
| Security   | Spring Security (form login, role-based)     |
| Database   | MySQL, Spring Data JPA / Hibernate           |
| Migrations | Flyway                                       |
| Frontend   | Thymeleaf, HTML, CSS, JavaScript             |
| Validation | Jakarta Bean Validation                      |
| Build tool | Maven (Maven Wrapper included)               |

---

## 🧠 OOP Concepts Used

The appointment module is designed around the four OOP pillars:

- **Abstraction** – `Appointment` is an abstract class; `AppointmentService` is an interface that controllers depend on.
- **Encapsulation** – All fields are private. State such as `status` and `rescheduleCount` can only change through business methods (`reschedule()`, `cancel()`, `changeStatus()`).
- **Inheritance** – `RegularAppointment` and `PremiumAppointment` extend `Appointment` (single-table inheritance with a discriminator column). `Admin` and `Customer` extend `User`.
- **Polymorphism** – `calculateTotalPrice()`, `getRescheduleCutoffHours()`, `getDurationMinutes()` and `getTypeLabel()` behave differently for each appointment type.

---

## 📋 Business Rules

### Appointment packages

| Package     | Price                | Duration                 | Reschedule allowed until |
|-------------|----------------------|--------------------------|--------------------------|
| **Regular** | Base price           | Service duration         | 24 hours before start    |
| **Premium** | Base price + 30%     | Service duration + 15 min consultation | 3 hours before start |

### Services (prices in LKR)

| Service                  | Price  | Duration |
|--------------------------|--------|----------|
| Haircut                  | 1,500  | 30 min   |
| Beard Trim & Shape       | 800    | 20 min   |
| Hot Towel Shave          | 1,000  | 30 min   |
| Haircut + Beard Combo    | 2,000  | 45 min   |
| Hair Colouring           | 3,500  | 60 min   |
| Men's Facial             | 3,000  | 45 min   |
| Head & Shoulder Massage  | 1,200  | 20 min   |

### Booking rules
- Salon hours: **9:00 AM – 7:00 PM**
- Appointments can be booked up to **60 days** in advance
- Available stylists: **Kasun, Nimal, Tharindu, Ashen**
- A stylist cannot be double-booked – overlapping time ranges are rejected
- Phone number must be 10 digits starting with `0`
- A rescheduled appointment goes back to `PENDING`

### Appointment status life-cycle

```
PENDING ──► CONFIRMED ──► COMPLETED
   │            │
   │            ├──► NO_SHOW
   ▼            ▼
CANCELLED ◄─────┘
```

---

## 📁 Project Structure

```
src/main/java/com/WD45/BeautySalonMaleSystem
├── config/          # Spring Security configuration
├── controller/      # Auth, Customer, Appointment, Admin controllers
├── dto/             # Data transfer objects & form models
├── entity/          # JPA entities (User, Admin, Customer, Appointment types, enums)
├── exception/       # Custom exceptions
├── repository/      # Spring Data JPA repositories
└── service/         # Service interfaces, implementations, file handler

src/main/resources
├── db/migration/    # Flyway SQL migrations (V1 – V6)
├── static/          # CSS, JS, images
├── templates/       # Thymeleaf views (admin/, customer/, login, register)
└── application.yml
```

---

## 🚀 Getting Started

### Prerequisites
- **JDK 21** or higher
- **MySQL 8+** running on `localhost:3306`
- Maven (or use the included `./mvnw`)

### 1. Clone the repository
```bash
git clone https://github.com/<your-username>/BeautySalonMaleSystem-AppointmentMangement.git
cd BeautySalonMaleSystem-AppointmentMangement
```

### 2. Configure the database
Edit `src/main/resources/application.yml` with your own MySQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/SaloonManagement?createDatabaseIfNotExist=true
    username: <your-mysql-username>
    password: <your-mysql-password>
```

The database is created automatically and the tables are set up by **Flyway** on first run.

### 3. Run the application
```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Open **http://localhost:8080** in your browser.

---

## 🔑 Default Admin Account

On first startup an admin account is created automatically:

| Email             | Password   |
|-------------------|------------|
| `admin@salon.com` | `admin123` |

> ⚠️ Change this password before using the system anywhere other than your local machine.

New customers can sign up from the **/register** page.

---

## 🗺️ Main Routes

| Route                           | Access   | Description                       |
|---------------------------------|----------|-----------------------------------|
| `/login`, `/register`           | Public   | Authentication                    |
| `/customer`                     | Customer | Customer home                     |
| `/customer/profile`             | Customer | View / edit profile               |
| `/appointments`                 | Logged-in | My appointments                  |
| `/appointments/book`            | Logged-in | Book an appointment              |
| `/appointments/{id}/reschedule` | Logged-in | Reschedule                       |
| `/dashboard`                    | Admin    | Admin dashboard (customers)       |
| `/admin/appointments`           | Admin    | Manage all appointments           |
| `/admin/appointments/file`      | Admin    | View file records                 |

---

## 💾 File Handling

After every create / update / delete, the full list of appointments is also written to a text file (default: `data/appointments.txt`), one record per line, fields separated by `|`:

```
id|type|customerName|customerEmail|phone|service|stylist|date|time|status|price|rescheduleCount
```

The path can be changed with the `app.appointments.file` property. The database remains the main data store.
