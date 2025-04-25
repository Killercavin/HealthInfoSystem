package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ClientDTO(
    val firstName: String,
    val lastName: String,
    val email: String
)

// Response DTO for client data, includes the client ID
@Serializable
data class ClientResponseDTO(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
)

// DTO for client enrollment with program ID
@Serializable
data class EnrollmentDTO(
    val programId: Int
)

// DTO for flexible search parameters
@Serializable
data class ClientSearchDTO(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val q: String? = null // General search query
)

// DTO for client profile data, including enrolled programs
@Serializable
data class ClientProfileDTO(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val programs: List<ProgramSummaryDTO>
)

// DTO for program summary in client profile
@Serializable
data class ProgramSummaryDTO(
    val id: Int,
    val name: String,
    val description: String?
)
