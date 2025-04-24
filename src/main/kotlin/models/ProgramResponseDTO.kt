// backend/src/main/kotlin/com/example/models/ProgramResponseDTO.kt
package com.example.models

import kotlinx.serialization.Serializable

/**
 * DTO for sending program data in responses.
 */
@Serializable
data class ProgramResponseDTO(
    val id: Int,
    val name: String,
    val description: String? = null
)
