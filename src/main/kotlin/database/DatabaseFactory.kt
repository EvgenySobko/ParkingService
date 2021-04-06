package database

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {

    fun init() {
        val url = "jdbc:postgresql://localhost:5432/parking_service"
        val driver = "org.postgresql.Driver"
        Database.connect(url, driver, "evgenysobko", "12345")
    }
}