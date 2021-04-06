import database.DatabaseFactory
import io.ktor.application.call
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8006, "bulochka.duckdns.org") {
        routing {
            get("/") {
                call.respond("ok")
            }
        }
    }.start(wait = true)
}