package com.example.models

import kotlinx.serialization.Serializable

/**
 * DTO for creating or receiving a health program.
 */
@Serializable
data class ProgramDTO(
    val name: String,
    val description: String? = null
)

/**
 * DTO for sending program data in responses.
 */
@Serializable
data class ProgramResponseDTO(
    val id: Int,
    val name: String,
    val description: String? = null
)