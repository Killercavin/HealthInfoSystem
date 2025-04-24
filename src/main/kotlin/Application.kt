package com.example

import com.example.configs.configureDatabase
import com.example.configs.configureRouting
import com.example.configs.configureSecurity
import io.ktor.server.application.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {})
    }
    configureRouting() // Install routing and wire up your route modules
    configureSecurity()
    configureDatabase() //  Configure DB (Hikari + Exposed + schema init)
}
