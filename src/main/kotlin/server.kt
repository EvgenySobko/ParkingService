import database.DatabaseFactory
import entities.getParking
import entities.getUsers
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8006, "bulochka.duckdns.org") {
        routing {
            get("/users") {
                call.respond("users = ${getUsers()}")
            }
            get("/parking") {
                call.respond("parking = ${getParking()}")
            }
        }
    }.start(wait = true)
}