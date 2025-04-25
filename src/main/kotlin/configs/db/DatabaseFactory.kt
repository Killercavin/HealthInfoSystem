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

/**
 * Handles database setup and connection management
 */
object DatabaseFactory {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Initializes the database connection and creates tables if they don't exist
     * Should be called once during application startup
     */
    fun init() {
        logger.info("Initializing SQLite database connection")

        try {
            // Connect to the database using Hikari pool
            Database.connect(hikari())

            // Create tables if not already present
            transaction {
                try {
                    SchemaUtils.create(Tables.Programs, Tables.Clients, Tables.Enrollments)
                    logger.info("Database tables created or verified successfully")
                } catch (e: Exception) {
                    logger.error("Error creating database tables: ${e.message}")
                }
            }

            logger.info("SQLite database connection initialized successfully")
        } catch (e: Exception) {
            // Handle connection failure
            logger.error("Failed to initialize database: ${e.message}")
            throw e
        }
    }

    /**
     * Configures the Hikari connection pool
     * @return A configured HikariDataSource
     */
    private fun hikari(): HikariDataSource {
        val dbPath = "./data/healthinfosystem.db"
        val maxPoolSize = 3  // SQLite allows only one write at a time

        logger.info("Connecting to SQLite database at $dbPath")

        val config = HikariConfig().apply {
            driverClassName = "org.sqlite.JDBC"
            jdbcUrl = "jdbc:sqlite:$dbPath"
            maximumPoolSize = maxPoolSize
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_SERIALIZABLE"
            validate()
        }

        return HikariDataSource(config)
    }

    /**
     * Executes database operations within a transaction on an IO thread
     * @param block The database operation to execute
     * @return The result of the database operation
     */
    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) { transaction { block() } }
}
