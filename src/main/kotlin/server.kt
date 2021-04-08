import database.DatabaseFactory
import entities.getParking
import entities.getUsers
import io.ktor.application.call
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8080) {
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