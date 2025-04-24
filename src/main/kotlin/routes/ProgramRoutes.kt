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

fun Route.programRoutes() {
    // Programs routes
    route("/api") {

        // Fetch all programs in a transaction
        route("/programs") {
            get {
                // Fetch all programs and map to DTOs
                val programs = transaction {
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

                // Respond with a homogeneous list of ProgramResponseDTO (all same type)
                call.respond(HttpStatusCode.OK, programs)
            }

            // POST - Create new program
            post {
                try {
                    val programDTO = call.receive<ProgramDTO>()

                    // Validation
                    if (programDTO.name.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Program name cannot be empty"))
                        return@post
                    }

                    // Insert into database
                    val newId = transaction {
                        Tables.Programs.insert {
                            it[name] = programDTO.name.trim()
                            it[description] = programDTO.description?.trim()
                        } get Tables.Programs.id
                    }

                    // Return success with the new ID
                    call.respond(HttpStatusCode.Created, mapOf("id" to newId))

                } catch (e: Exception) {
                    application.log.error("Failed to create program", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Server error: ${e.message}"))
                }
            }
            route("/{id}") {
                get {
                    // Get a specific program
                }
                put {
                    // Update a program
                }
                delete {
                    // Delete a program
                }
            }
        }
    }
}