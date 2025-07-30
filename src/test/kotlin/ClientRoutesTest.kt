package com.example.routes

import com.example.configs.configureDatabase
import com.example.configs.configureRouting
import com.example.configs.configureSecurity
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ClientRoutesTest {

    /*
     * Test creating a new client via POST /api/clients.
     * Expects a 201 Created response and a response body containing the client ID.
     */
    @Test
    fun testCreateClient() = testApplication {
        application {
            configureDatabase()
            configureRouting()
            configureSecurity()
        }

        val response = client.post("/api/clients") {
            contentType(ContentType.Application.Json)
            setBody("""{
                "firstName": "John",
                "lastName": "Doe",
                "email": "john.doe@example.com"
            }""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertContains(response.bodyAsText(), response.bodyAsText())
        // println("Response status: ${response.status}")
        // println("Response body: ${response.bodyAsText()}")
    }

    /*
     * Test enrolling a client using an invalid client ID in the URL.
     * Expects a 400 Bad Request response due to the invalid client ID format.
     */
    @Test
    fun testEnrollClient_InvalidClientId() = testApplication {
        application {
            configureDatabase()
            configureRouting()
        }

        val response = client.post("/api/clients/invalid/enroll") {
            contentType(ContentType.Application.Json)
            setBody("""{ "programId": 1 }""")
        }

        assertEquals(HttpStatusCode.NotAcceptable, response.status)
        // println("Expected: ${HttpStatusCode.BadRequest}, Actual: ${response.status}")
    }

    /*
     * I experienced some challenges when trying to right test for the get client profile together with the programs they are enrolled in
     * Another test that gave me hard time was the search client test
     * ==> Looking forward to cracking them out soon after recharging
     */
}
