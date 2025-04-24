package com.example.configs

import com.example.routes.programRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") // Loading the static

        // API endpoints
        programRoutes()

    }
}
