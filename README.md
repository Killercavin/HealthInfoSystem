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
   ├─ Database.kt               # configureDatabase() extension
   ├─ Routing.kt                # programRoutes(), clientRoutes(), enrollmentRoutes()
   ├─ Tables.kt                 # Exposed table definitions
   └─ models/
      ├─ ProgramDTO.kt          # @Serializable for POST /api/programs
      ├─ ProgramResponseDTO.kt  # @Serializable for GET /api/programs
      ├─ ClientDTO.kt           # upcoming…
      └─ EnrollmentDTO.kt        # upcoming…
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
   The SQLite file is created automatically at `./data/healthinfosystem.db`.

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

## Next Steps

- **Client Registration** (`POST /api/clients`)
- **Enroll Client** (`POST /api/clients/{id}/enroll`)
- **Search Clients** (`GET /api/clients?q=…`)
- **View Client Profile** (`GET /api/clients/{id}`)
- **Simple Static UI** under `resources/static/`

---

> _Progress so far:_  
> – Database setup with HikariCP & Exposed  
> – ContentNegotiation + CallLogging installed  
> – Program DTOs, table, routes implemented and tested

Feel free to run the existing endpoints, and watch this space as we flesh out clients and enrollments next!

