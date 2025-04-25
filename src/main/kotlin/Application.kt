package com.example

import com.example.configs.configureDatabase
import com.example.configs.configureRouting
import com.example.configs.configureSecurity
import io.ktor.server.application.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*

/**
 * Application entry point. Starts the server by calling the module function.
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Application module. Sets up server features like CORS, routing, security, and database.
 */
fun Application.module() {
    // Configure CORS to allow cross-origin requests from example localhost:8080 or any server url
    install(CORS) {
        allowHost("localhost:8080", schemes = listOf("http"))
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
    }

    // Configure JSON serialization/deserialization
    install(ContentNegotiation) {
        json(Json { })
    }

    // Set up application components
    configureRouting()   // API routing
    configureSecurity()  // Authentication and authorization
    configureDatabase()  // Database connection and setup
}
