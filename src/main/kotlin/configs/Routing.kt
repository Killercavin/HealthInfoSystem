package com.example.configs

import com.example.routes.clientRoutes
import com.example.routes.programRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") // Serve static files (HTML, CSS, JS)

        // Define API routes
        programRoutes() // Routes for programs
        clientRoutes()  // Routes for clients
    }
}
