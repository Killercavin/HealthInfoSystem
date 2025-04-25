package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ClientDTO(
    val firstName: String,
    val lastName: String,
    val email: String
)

// client response data transfer object
@Serializable
data class ClientResponseDTO(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
)

// client enrollment DTO
@Serializable
data class EnrollmentDTO(
    val programId: Int
)

// search DTO to handle flexible search parameters
@Serializable
data class ClientSearchDTO(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val q: String? = null // General search query parameter
)

// a DTO for the client profile objects
@Serializable
data class ClientProfileDTO(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val programs: List<ProgramSummaryDTO>
)

// program summary DTO
@Serializable
data class ProgramSummaryDTO(
    val id: Int,
    val name: String,
    val description: String?
)