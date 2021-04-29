package database

import org.jetbrains.exposed.sql.Database

class DatabaseFactory {

    fun init() {
        val url = "jdbc:postgresql://localhost:5432/postgres"
        val driver = "org.postgresql.Driver"
        Database.connect(url, driver, "postgres", "12345")
    }
}