package com.example.configs.db

import com.example.configs.Tables
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseFactory {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun init() {
        logger.info("Initializing SQLite database connection")

        try {
            Database.connect(hikari())

            transaction {
                // Create tables with error handling
                try {
                    SchemaUtils.create(
                        Tables.Programs,
                        Tables.Clients,
                        Tables.Enrollments
                    )
                    logger.info("Database tables created or verified successfully")
                } catch (e: Exception) {
                    logger.error("Error creating database tables: ${e.message}")
                }
            }

            logger.info("SQLite database connection initialized successfully")
        } catch (e: Exception) {
            logger.error("Failed to initialize database: ${e.message}")
            throw e  // Re-throwing to prevent application startup with broken DB
        }
    }

    private fun hikari(): HikariDataSource {
        val dbPath = "./data/healthinfosystem.db"
        val maxPoolSize = 3

        logger.info("Connecting to SQLite database at $dbPath")

        val config = HikariConfig().apply {
            driverClassName = "org.sqlite.JDBC"
            jdbcUrl = "jdbc:sqlite:$dbPath"
            maximumPoolSize = maxPoolSize  // SQLite supports limited concurrency
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_SERIALIZABLE"  // SQLite's default
            validate()
        }

        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}