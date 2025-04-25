package com.example.configs

import com.example.configs.db.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabase() {
    // Initialize database connection
    return DatabaseFactory.init()
}
