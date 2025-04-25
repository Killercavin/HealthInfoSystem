package com.example.routes

import com.example.configs.Tables
import com.example.models.ClientDTO
import com.example.models.ClientProfileDTO
import com.example.models.ClientResponseDTO
import com.example.models.ProgramSummaryDTO
import com.example.models.EnrollmentDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Defines client-related API routes
 */
fun Route.clientRoutes() {
    route("/api/clients") {
        // GET operations
        configureClientGetOperations()

        // POST operations
        configureClientPostOperations()
    }
}

/**
 * Configures GET endpoints for clients
 */
private fun Route.configureClientGetOperations() {
    // Get all clients or search by query parameter
    get {
        val query = call.request.queryParameters["q"]?.trim()?.lowercase()
        val clients = searchClients(query)
        call.respond(HttpStatusCode.OK, clients)
    }

    // Advanced search with filters for first name, last name, and email
    get("/search") {
        val clients = advancedSearchClients(
            firstName = call.request.queryParameters["firstName"]?.trim()?.lowercase(),
            lastName = call.request.queryParameters["lastName"]?.trim()?.lowercase(),
            email = call.request.queryParameters["email"]?.trim()?.lowercase()
        )
        call.respond(HttpStatusCode.OK, clients)
    }

    // Get client profile by ID
    get("/{id}") {
        val clientId = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid client ID"))

        val clientProfile = getClientProfile(clientId)
            ?: return@get call.respond(HttpStatusCode.NotFound, errorResponse("Client not found"))

        call.respond(HttpStatusCode.OK, clientProfile)
    }
}

/**
 * Configures POST endpoints for clients
 */
private fun Route.configureClientPostOperations() {
    // Create a new client
    post {
        try {
            val clientDTO = call.receive<ClientDTO>()
            if (!validateClientData(clientDTO)) {
                call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid client data"))
                return@post
            }
            val response = createClient(clientDTO)
            call.respond(HttpStatusCode.Created, response)
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid request format"))
        } catch (e: Exception) {
            call.application.log.error("Error creating client", e)
            call.respond(HttpStatusCode.InternalServerError, errorResponse("Failed to create client"))
        }
    }

    // Enroll a client in a program
    post("/{id}/enroll") {
        val clientId = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid client ID"))

        val enrollmentDTO = try {
            call.receive<EnrollmentDTO>().also {
                require(it.programId > 0) { "Program ID must be valid" }
            }
        } catch (e: Exception) {
            return@post call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid request body"))
        }

        try {
            enrollClientInProgram(clientId, enrollmentDTO.programId)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Client enrolled successfully"))
        } catch (e: Exception) {
            application.log.error("Enrollment failed", e)
            call.respond(HttpStatusCode.InternalServerError, errorResponse("Could not enroll client"))
        }
    }
}

/**
 * Search for clients based on a general query
 */
private fun searchClients(query: String?): List<ClientResponseDTO> = transaction {
    (if (query.isNullOrBlank()) {
        Tables.Clients.selectAll()
    } else {
        Tables.Clients.select {
            (Tables.Clients.firstName.lowerCase() like "%$query%") or
                    (Tables.Clients.lastName.lowerCase() like "%$query%") or
                    (Tables.Clients.email.lowerCase() like "%$query%")
        }
    }).map { mapToClientResponseDTO(it) }
}

/**
 * Advanced search for clients with filters for first name, last name, and email
 */
private fun advancedSearchClients(
    firstName: String?,
    lastName: String?,
    email: String?
): List<ClientResponseDTO> = transaction {
    val query = Tables.Clients.selectAll()
    query.andWhere {
        var condition: Op<Boolean>? = null
        if (!firstName.isNullOrBlank()) {
            condition = Tables.Clients.firstName.lowerCase() like "%$firstName%"
        }
        if (!lastName.isNullOrBlank()) {
            val lastNameCondition = Tables.Clients.lastName.lowerCase() like "%$lastName%"
            condition = condition?.and(lastNameCondition) ?: lastNameCondition
        }
        if (!email.isNullOrBlank()) {
            val emailCondition = Tables.Clients.email.lowerCase() like "%$email%"
            condition = condition?.and(emailCondition) ?: emailCondition
        }
        condition ?: Op.TRUE
    }
    query.map { mapToClientResponseDTO(it) }
}

/**
 * Get client profile data, including enrolled programs
 */
private fun getClientProfile(clientId: Int): ClientProfileDTO? = transaction {
    val clientRow = Tables.Clients.select { Tables.Clients.id eq clientId }.singleOrNull()
        ?: return@transaction null

    val enrolledPrograms = Tables.Enrollments
        .innerJoin(Tables.Programs)
        .select { Tables.Enrollments.clientId eq clientId }
        .map {
            ProgramSummaryDTO(
                id = it[Tables.Programs.id],
                name = it[Tables.Programs.name],
                description = it[Tables.Programs.description]
            )
        }

    ClientProfileDTO(
        id = clientRow[Tables.Clients.id],
        firstName = clientRow[Tables.Clients.firstName],
        lastName = clientRow[Tables.Clients.lastName],
        email = clientRow[Tables.Clients.email],
        programs = enrolledPrograms
    )
}

/**
 * Validate required client data
 */
private fun validateClientData(clientDTO: ClientDTO): Boolean {
    if (clientDTO.firstName.isBlank() || clientDTO.lastName.isBlank() || clientDTO.email.isBlank()) {
        return false
    }
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    return clientDTO.email.matches(emailRegex)
}

/**
 * Create a new client in the database and return the response DTO
 */
private fun createClient(clientDTO: ClientDTO): ClientResponseDTO {
    val id = transaction {
        Tables.Clients.insert {
            it[firstName] = clientDTO.firstName
            it[lastName] = clientDTO.lastName
            it[email] = clientDTO.email
        } get Tables.Clients.id
    }
    return ClientResponseDTO(
        id = id,
        firstName = clientDTO.firstName,
        lastName = clientDTO.lastName,
        email = clientDTO.email
    )
}

/**
 * Enroll a client in a program
 */
private fun enrollClientInProgram(clientId: Int, programId: Int) {
    transaction {
        Tables.Enrollments.insert {
            it[Tables.Enrollments.clientId] = clientId
            it[Tables.Enrollments.programId] = programId
        }
    }
}

/**
 * Map a database row to a ClientResponseDTO
 */
private fun mapToClientResponseDTO(row: ResultRow): ClientResponseDTO =
    ClientResponseDTO(
        id = row[Tables.Clients.id],
        firstName = row[Tables.Clients.firstName],
        lastName = row[Tables.Clients.lastName],
        email = row[Tables.Clients.email]
    )

/**
 * Standardized error response
 */
private fun errorResponse(message: String): Map<String, String> =
    mapOf("error" to message)
