# MysteryBox — Backend (Spring Boot 3 & Java 21)

> **Interactive Mystery Investigation and Case Resolution Platform**  
> A full-stack web application developed by **K. V. GANESH KOUSHAL** (Roll No: **24EG104A13**, ECE Department, Anurag University).

---

## 🛡️ Overview

**MysteryBox Backend** is a high-performance RESTful API built with **Spring Boot 3** and **Java 21**. It powers the mystery investigation platform by managing criminal cases, clues, evidence, user progress, and server-side answer evaluation.

---

## 🚀 Key Features

- **Stateless JWT & BCrypt Security:** Passwords are encrypted using BCrypt. Protected endpoints require a valid Bearer token validated via `JwtAuthenticationFilter`.
- **Role-Based Authorization:** Separate access controls for `ROLE_USER` (Investigator) and `ROLE_ADMIN` (Chief Administrator).
- **Server-Side Answer Validation:** Solution answers are strictly never exposed through public case endpoints. Submitted deductions are validated on the server using normalized string comparison.
- **Score Calculation & Duplicate Prevention:** Correct answers award 100 investigation points. The database enforces a unique constraint on `(user_id, case_id)` to prevent multiple score awards for the same case.
- **Automated Data Seeding (`DataInitializer`):** On startup, automatically creates:
  - Default Admin: `admin@mysterybox.com` (`Admin@123`)
  - Demo Investigator: `holmes@test.com` (`Secret@123`)
  - Three complete mystery cases with sequential clues, physical evidence, and solutions.
- **Dual Profiles (Cloud & Offline Dev):**
  - `dev` (Default): Uses in-memory H2 database with PostgreSQL compatibility mode for instant local testing.
  - `prod`: Connects to managed PostgreSQL on Supabase via SSL.
- **Docker Containerization:** Multi-stage Dockerfile based on Eclipse Temurin JDK 21.

---

## 🛠️ Technology Stack

- **Language:** Java 21 (JDK 21 LTS)
- **Framework:** Spring Boot 3.3.4
- **Security:** Spring Security & JJWT (io.jsonwebtoken 0.11.5)
- **Persistence:** Spring Data JPA (Hibernate 6)
- **Database:** PostgreSQL (Supabase) / H2 (Development)
- **Build Tool:** Maven Wrapper (`mvnw` / `mvnw.cmd`)
- **Containerization:** Docker (Eclipse Temurin 21 Alpine)
- **Hosting Platform:** Render (Web Service)

---

## 📁 Package Architecture

```
com.mysterybox/
├── config/
│   └── DataInitializer.java     # Seeds admin, demo sleuth, and 3 sample cases
├── controller/
│   ├── AuthController.java      # /api/auth (register, login, me)
│   ├── CaseController.java      # /api/cases (public list, details, submit)
│   ├── ProgressController.java  # /api/progress (user score & rank)
│   ├── AdminCaseController.java # /api/admin/cases (case CRUD with solutions)
│   └── HealthController.java    # /api/health (service health probe)
├── dto/                         # Request and response models (solutions masked)
├── entity/                      # JPA entities (User, Case, Clue, Evidence, Solution, Progress)
├── exception/
│   └── GlobalExceptionHandler.java # Sanitized error responses
├── repository/                  # Spring Data JPA repositories
├── security/                    # JwtUtils, JwtFilter, CustomUserDetailsService, SecurityConfig
└── service/                     # Business logic and answer evaluation rules
```

---

## 📡 REST API Specification

### Authentication & Profile
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register a new user |
| `POST` | `/api/auth/login` | Public | Authenticate and obtain JWT |
| `GET` | `/api/auth/me` | Authenticated | Retrieve current user profile |

### Investigation & Cases
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/cases` | Public | List all active cases (solution omitted) |
| `GET` | `/api/cases/{id}` | Public | Retrieve case dossier, clues, and evidence |
| `GET` | `/api/cases/{id}/clues` | Public | Retrieve chronological clues for a case |
| `GET` | `/api/cases/{id}/evidence` | Public | Retrieve forensic evidence items |
| `POST` | `/api/cases/{id}/submit` | Investigator | Submit deduction; awards 100 points if correct |
| `GET` | `/api/progress` | Investigator | Retrieve authenticated user's points & rank |

### Chief Administration
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/admin/cases` | Admin Only | List cases including correct solutions |
| `GET` | `/api/admin/cases/{id}` | Admin Only | Get single case with solution |
| `POST` | `/api/admin/cases` | Admin Only | Create case with clues, evidence, and solution |
| `PUT` | `/api/admin/cases/{id}` | Admin Only | Update case details, clues, and evidence |
| `DELETE` | `/api/admin/cases/{id}` | Admin Only | Delete case and associated records |

---

## ⚙️ Environment Variables

| Variable | Description | Default Local Value |
|---|---|---|
| `PORT` | HTTP Server port | `8080` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile (`dev` or `prod`) | `dev` |
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC Connection String | `jdbc:postgresql://localhost:5432/mysterybox` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | Signing secret key (min 256-bit) | `MysteryBoxSuperSecretKeyForJwtSigning1234567890!@#$` |
| `JWT_EXPIRATION_MS` | Token expiry duration in ms | `86400000` (24h) |
| `CORS_ALLOWED_ORIGINS` | Permitted frontend origins | `http://localhost:5173,http://localhost:3000` |
| `ADMIN_DEFAULT_EMAIL` | Auto-seeded admin email | `admin@mysterybox.com` |
| `ADMIN_DEFAULT_PASSWORD` | Auto-seeded admin password | `Admin@123` |

---

## 💻 Local Setup & Execution

### Prerequisites
- Java 21+ (Eclipse Adoptium Temurin recommended)
- Maven Wrapper is included (`mvnw` / `mvnw.cmd`)

### Running Locally (Default In-Memory Dev Profile)
```powershell
cd backend
.\mvnw.cmd spring-boot:run
```
*API will be available at `http://localhost:8080`.*

### Running Automated Test Suite
```powershell
.\mvnw.cmd test
```

### Packaging Production JAR
```powershell
.\mvnw.cmd clean package -DskipTests
```
*Outputs `target/mysterybox-backend.jar`.*

---

## 🐳 Docker Containerization

### Build Image
```bash
docker build -t mysterybox-backend .
```

### Run Container
```bash
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres?sslmode=require" \
  -e SPRING_DATASOURCE_USERNAME="postgres.xxxxxx" \
  -e SPRING_DATASOURCE_PASSWORD="your_password" \
  -e JWT_SECRET="Your32CharacterSecretKey1234567890!" \
  -e CORS_ALLOWED_ORIGINS="https://mysterybox-frontend.onrender.com" \
  --name mysterybox-api \
  mysterybox-backend
```

---

## 🌐 Render Cloud Deployment

1. Create a **New Web Service** on [Render](https://dashboard.render.com).
2. Connect your `mysterybox-backend` GitHub repository.
3. Choose the **Docker** runtime (Render automatically detects `backend/Dockerfile`).
4. Set the following environment variables in Render:
   - `SPRING_PROFILES_ACTIVE` = `prod`
   - `SPRING_DATASOURCE_URL` = `jdbc:postgresql://db.xxxxxx.supabase.co:5432/postgres?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME` = `postgres`
   - `SPRING_DATASOURCE_PASSWORD` = `[your-supabase-password]`
   - `JWT_SECRET` = `[your-32-character-secret]`
   - `CORS_ALLOWED_ORIGINS` = `https://mysterybox-frontend.onrender.com`
5. Click **Deploy Web Service**.

---

## 👤 Author & Academic Details

- **Student Name:** K. V. GANESH KOUSHAL
- **Roll Number:** 24EG104A13
- **College Email:** 24EG104A13@ANURAG.EDU.IN
- **Branch / Section:** ECE — Section A
- **Project Title:** MysteryBox — Interactive Mystery Investigation and Case Resolution Platform
