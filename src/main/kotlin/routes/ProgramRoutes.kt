package com.example.routes

import com.example.configs.Tables
import com.example.models.ProgramDTO
import com.example.models.ProgramResponseDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Program API routes handler
 *
 * Provides endpoints for:
 * - Listing all programs
 * - Creating new programs
 */
fun Route.programRoutes() {
    route("/api/programs") {
        // GET - Fetch all programs
        get {
            val programs = getAllPrograms()
            call.respond(HttpStatusCode.OK, programs)
        }

        // POST - Create new program
        post {
            try {
                val programDTO = call.receive<ProgramDTO>()

                // Validate program data
                if (!validateProgramData(programDTO)) {
                    call.respond(HttpStatusCode.BadRequest, errorResponse("Program name cannot be empty"))
                    return@post
                }

                // Create program and return response
                val newId = createProgram(programDTO)
                call.respond(HttpStatusCode.Created, mapOf("id" to newId))

            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest,
                    errorResponse("Invalid request format. Required field: name"))
            } catch (e: Exception) {
                application.log.error("Failed to create program", e)
                call.respond(HttpStatusCode.InternalServerError,
                    errorResponse("Server error: ${e.message}"))
            }
        }
    }
}

/**
 * Retrieves all programs from the database
 *
 * @return List of program DTOs
 */
private fun getAllPrograms(): List<ProgramResponseDTO> = transaction {
    Tables.Programs
        .selectAll()
        .map { row ->
            ProgramResponseDTO(
                id = row[Tables.Programs.id],
                name = row[Tables.Programs.name],
                description = row[Tables.Programs.description]
            )
        }
}

/**
 * Validates program data before creating a new program
 *
 * @param programDTO The program data to validate
 * @return true if validation passes, false otherwise
 */
private fun validateProgramData(programDTO: ProgramDTO): Boolean {
    return programDTO.name.isNotBlank()
}

/**
 * Creates a new program in the database
 *
 * @param programDTO The program data to insert
 * @return ID of the newly created program
 */
private fun createProgram(programDTO: ProgramDTO): Int = transaction {
    Tables.Programs.insert {
        it[name] = programDTO.name.trim()
        it[description] = programDTO.description?.trim()
    } get Tables.Programs.id
}

/**
 * Creates a standardized error response
 *
 * @param message The error message
 * @return Map with error field
 */
private fun errorResponse(message: String): Map<String, String> =
    mapOf("error" to message)