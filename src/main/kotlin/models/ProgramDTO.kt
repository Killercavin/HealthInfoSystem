package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ProgramDTO(
    val name: String,
    val description: String? = null
)

/**
 * DTO for program data in responses, includes program ID.
 */
@Serializable
data class ProgramResponseDTO(
    val id: Int,
    val name: String,
    val description: String? = null
)
