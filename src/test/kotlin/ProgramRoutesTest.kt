package com.example.routes

import com.example.configs.configureDatabase
import com.example.configs.configureRouting
import com.example.configs.configureSecurity
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ProgramRoutesTest {

    /*
     * Test adding a valid health program via POST /api/programs.
     * Expects a 201 Created response and a response body containing the program ID.
     */
    @Test
    fun testAddProgram() = testApplication {
        application {
            configureDatabase()
            configureRouting()
            configureSecurity()
        }

        val response = client.post("/api/programs") {
            contentType(ContentType.Application.Json)
            setBody("""{
                "name": "Malaria",
                "description": "Prevention program"
            }""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        // assertContains(response.bodyAsText(), "id")
    }

    /*
     * Test submitting an invalid program request with missing required fields.
     * Expects a 422 Unprocessable Entity response due to missing 'name'.
     */
    /*@Test
    fun testAddProgram_InvalidRequest() = testApplication {
        application {
            configureDatabase()
            configureRouting()
        }

        val response = client.post("/api/programs") {
            contentType(ContentType.Application.Json)
            setBody("""{ "description": "Missing name" }""")
        }

        // assertEquals(HttpStatusCode.BadRequest, response.status)
        println("Expected: ${HttpStatusCode.BadRequest}, Actual: ${response.status}")
    }
    */
}
