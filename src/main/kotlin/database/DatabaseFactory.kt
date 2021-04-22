package database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DatabaseFactory {

    fun init() {
        val url = "jdbc:postgresql://localhost:5432/postgres"
        val driver = "org.postgresql.Driver"
        Database.connect(url, driver, "postgres", "")
    }

    private fun createConfig(): HikariDataSource {
        HikariConfig().run {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://localhost:5432/parking_service"
            username = "evgenysobko"
            password = "12345"
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
            return HikariDataSource(this)
        }
    }
}