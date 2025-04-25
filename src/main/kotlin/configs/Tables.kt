package com.example.configs

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Tables {

    // Table for health programs (e.g., TB, Malaria)
    object Programs : Table("programs") {
        val id          = integer("id").autoIncrement()
        val name        = varchar("name", 100)
        val description = text("description").nullable()
        override val primaryKey = PrimaryKey(id)
    }

    // Table for client data
    object Clients : Table("clients") {
        val id        = integer("id").autoIncrement()
        val firstName = varchar("first_name", 50)
        val lastName  = varchar("last_name", 50)
        val email     = varchar("email", 100).uniqueIndex()
        override val primaryKey = PrimaryKey(id)
    }

    // Table for program enrollments
    object Enrollments : Table("enrollments") {
        val id        = integer("id").autoIncrement()
        val clientId  = integer("client_id").references(Clients.id, onDelete = ReferenceOption.CASCADE)
        val programId = integer("program_id").references(Programs.id, onDelete = ReferenceOption.CASCADE)
        val enrolledAt = timestamp("enrolled_at").clientDefault { Instant.now() }

        override val primaryKey = PrimaryKey(id)
    }

}
