package com.example.configs

import com.example.configs.db.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabase() {
    return DatabaseFactory.init() // Initializing the database
}