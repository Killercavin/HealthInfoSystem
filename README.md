# HealthInfoSystem

A minimal Health Information System built with Ktor (Kotlin) and Exposed ORM, focused on an API-first development approach.

## Tech Stack

- **Backend:** Ktor, Exposed ORM, HikariCP, SQLite
- **Serialization & Logging:** kotlinx-serialization, ContentNegotiation, CallLogging
- **Frontend:** Static HTML/CSS/JS served via Ktor’s `static` plugin (partially included — home and programs pages only)
- **Testing:** Ktor server testing framework + Kotlin test

## Project Structure

```
backend/
└─ src/main/kotlin/com/example/
   ├─ Application.kt             # Server bootstrap: DB setup, plugins, routing
   └─ configs/
      ├─ Database.kt             # configureDatabase() extension
      ├─ Routing.kt              # programRoutes(), clientRoutes(), enrollmentRoutes()
      ├─ Security.kt             # (not used — retained for future extension)
      ├─ Tables.kt               # Exposed table definitions
      └─ db/
         └─ DatabaseFactory.kt   # Exposed database configurations
   ├─ models/
      ├─ ProgramDTO.kt           # @Serializable for POST /api/programs
      ├─ ClientDTO.kt            # For client creation
      ├─ ClientResponseDTO.kt    # For search results
      ├─ ClientProfileDTO.kt     # For profile with enrolled programs
      ├─ EnrollmentDTO.kt        # For client enrollment into a program
   ├─ routes/
      ├─ ProgramRoutes.kt        # GET and POST /api/programs
      └─ ClientRoutes.kt         # Client-related endpoints: create, search, profile
test/
   ├─ ProgramRoutesTest.kt       # Unit tests for program routes
   └─ ClientRoutesTest.kt        # Unit tests for client and enrollment logic
resources/
└─ static/                       # Static UI files (partially used)
build.gradle.kts                 # Dependencies & Kotlin serialization plugin
docs/
   ├─ HealthInfoSystem.pptx       # Presentation PowerPoint file
   └─ HealthInfoSystem_Demo.mp4   # Video prototype demonstration
```

## Setup & Run

1. **Clone the repo**
   ```bash
   git clone git@github.com:Killercavin/HealthInfoSystem.git
   cd HealthInfoSystem
   ```

2. **Build, Run, and Test**
   ```bash
   ./gradlew clean build   # Clean and build
   ./gradlew run           # Start the server
   ./gradlew test          # Run tests
   ```
   The server listens on `http://localhost:8080`.

3. **Database**  
   The SQLite file is automatically created at `./data/`.

## Presentation and Demonstration

### 📁 Presentation

Access the full project presentation explaining the approach, design, and solution here:  
➡️ [`HealthInfoSystem_Presentation.pptx`](docs/HealthInfoSystem_Presentation.pptx)

---

### 📹 Demo Video

Watch the full prototype demonstration here:  
➡️ [`HealthInfoSystem_Demo.mp4`](docs/HealthInfoSystem_Demo.mp4)  
_<!-- (Or access via [Google Drive/hosted link if file too large](YOUR_GOOGLE_DRIVE_OR_LOOM_LINK))-->_

---

## Implemented Endpoints

### ➔ Add a Program

- **POST** `/api/programs`
- **Request Body:**
  ```json
  {
    "name": "Malaria",
    "description": "Prevention and treatment"
  }
  ```
- **Responses:**
    - `201 Created`
    - `400 Bad Request` for malformed JSON
    - `422 Unprocessable Entity` if `name` is blank
    - `500 Internal Server Error` on DB failure

---

### ➔ List All Programs

- **GET** `/api/programs`
- **Response:**
  ```json
  [
    { "id": 1, "name": "TB", "description": "Tuberculosis program" },
    { "id": 2, "name": "Malaria", "description": "Malaria prevention" }
  ]
  ```

---

### ➔ Create Client

- **POST** `/api/clients`
- **Request Body:**
  ```json
  {
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane.doe@example.com"
  }
  ```
- **Response:**
  ```json
  { "id": 1 }
  ```

---

### ➔ Enroll Client

- **POST** `/api/clients/{id}/enroll`
- **Request Body:**
  ```json
  {
    "programId": 2
  }
  ```
- **Response:**
  ```json
  { "message": "Client enrolled successfully" }
  ```

---

### ➔ Search Clients

- **GET** `/api/clients?q=searchText`
- **Response:**
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

---

### ➔ View Client Profile (Exposed to External Systems)

- **GET** `/api/clients/{id}`
- **Response:**
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

---

## Final Status

- ✅ All required API endpoints implemented
- ✅ Client profile successfully exposed for integration
- ✅ CORS enabled for `localhost:8080` to allow external system access
- ✅ API tests written for program and client workflows
- ✅ Fully focused and delivered API-first implementation
- ✅ Presentation and prototype demonstration completed

---

> _Progress Summary:_  
> ✓ Full backend setup with Ktor, Exposed & SQLite  
> ✓ Program and Client CRUD features tested  
> ✓ Enrollment and profile APIs connected  
> ✓ Automated test cases written  
> ✓ Documentation, presentation, and prototype ready

