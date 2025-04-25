# HealthInfoSystem

A minimal Health Information System built with Ktor (Kotlin) and Exposed ORM, plus static files for a simple UI.

## Tech Stack

- **Backend:** Ktor, Exposed, HikariCP, SQLite
- **Serialization & Logging:** kotlinx-serialization, ContentNegotiation, CallLogging
- **Frontend:** Static HTML/CSS/JS served via Ktor’s `static` plugin

## Project Structure

```
backend/
└─ src/main/kotlin/com/example/
   ├─ Application.kt            # Server bootstrap: DB setup, plugins, routing
   └─ configs/
      ├─ Database.kt            # configureDatabase() extension
      ├─ Routing.kt             # programRoutes(), clientRoutes(), enrollmentRoutes()
      ├─ Security.kt            # Security for the exposed endpoint - upcoming...
      ├─ Tables.kt              # Exposed table definitions
      └─ db/
         ├─ DatabaseFactory.kt  # Exposed database configurations
   └─ models/
      ├─ ProgramDTO.kt          # @Serializable for POST /api/programs
      ├─ ClientDTO.kt           # For client creation
      ├─ ClientResponseDTO.kt   # For search
      ├─ ClientProfileDTO.kt    # For profile with enrolled programs
      ├─ EnrollmentDTO.kt       # For client enrollment into a program
   └─ routes/
      ├─ ProgramRoutes.kt       # GET and POST /api/programs
      ├─ ClientRoutes.kt        # Client-related endpoints: create, search, profile
resources/
└─ static/                      # Static UI files (HTML/CSS/JS)
build.gradle.kts                # Dependencies & Kotlin serialization plugin
```

## Setup & Run

1. **Clone the repo**
   ```bash
   git clone git@github.com:Killercavin/HealthInfoSystem.git
   cd HealthInfoSystem
   ```

2. **Build & Run**
   ```bash
   ./gradlew clean build
   ./gradlew run
   ```
   The server listens on `http://localhost:8080`.

3. **Database**  
   The SQLite file is created automatically at `./data`.

## Implemented Endpoints

### Add a Program

- **POST** `/api/programs`
- **Request Body**
  ```json
  {
    "name": "Malaria",
    "description": "Prevention and treatment"
  }
  ```
- **Responses**
    - `201 Created`
      ```json
      { "id": 1 }
      ```  
    - `400 Bad Request` for malformed JSON
    - `422 Unprocessable Entity` if `name` is blank
    - `500 Internal Server Error` on DB failure

### List All Programs

- **GET** `/api/programs`
- **Response** `200 OK`
  ```json
  [
    { "id": 1, "name": "TB", "description": "Tuberculosis program" },
    { "id": 2, "name": "Malaria", "description": "Malaria prevention" }
  ]
  ```

### Create Client

- **POST** `/api/clients`
- **Request Body**
  ```json
  {
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane.doe@example.com"
  }
  ```
- **Response**
  ```json
  { "id": 1 }
  ```

### Enroll Client

- **POST** `/api/clients/{id}/enroll`
- **Request Body**
  ```json
  {
    "programId": 2
  }
  ```
- **Response**
  ```json
  { "message": "Client enrolled successfully" }
  ```

### Search Clients

- **GET** `/api/clients?q=searchText`
- **Response**
  ```json
  [
    {
      "id": 1,
      "firstName": "Jane",
      "lastName": "Doe",
      "email": "jane.doe@example.com"
    }
  ]
  ```

### View Client Profile

- **GET** `/api/clients/{id}`
- **Response**
  ```json
  {
    "id": 1,
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane.doe@example.com",
    "programs": [
      {
        "id": 2,
        "name": "Malaria",
        "description": "Prevention and treatment"
      }
    ]
  }
  ```

## Next Steps

- **Enable CORS for external system access** (in progress)
- **Secure the exposed endpoints** (`Security.kt`) – upcoming
- **Simple Static UI** under `resources/static/`

---

> _Progress so far:_  
> ✓ Database setup with HikariCP & Exposed  
> ✓ ContentNegotiation + CallLogging installed  
> ✓ Program and Client DTOs, tables, and routes implemented  
> ✓ Client enrollment and profile API added  
> ✓ Ready for cross-origin access (CORS setup next)

