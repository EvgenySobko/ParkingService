import database.DatabaseFactory
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import utils.DateTime
import utils.Respond
import utils.Validator

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8006) {
        routing {
            post("/park") {
                val partData = call.receiveMultipart().readPart()
                if (partData is PartData.FormItem) {
                    val carNumber = partData.value
                    if (Validator.validateCarNumber(carNumber)) {
                        // TODO: registerParking()
                        call.response.status(HttpStatusCode.OK)
                        call.respondText(Respond.parkingTimeRespond(DateTime.getCurrentDateAndTime()))
                    } else {
                        call.respond(HttpStatusCode(400, Respond.ICN))
                    }
                }

            }
            post("/unpark") {
                val partData = call.receiveMultipart().readPart()
                if (partData is PartData.FormItem) {
                    val carNumber = partData.value
                    if (Validator.validateCarNumber(carNumber)) {
                        // TODO: checkIfCarIsNotParked
                        // TODO: calculateSummary
                        val dummySummary = 1000
                        call.respondText(Respond.summaryRespond(dummySummary))
                    } else {
                        call.respond(HttpStatusCode(400, Respond.ICN))
                    }
                }
            }
            post("/pay") {
                val multipartData = call.receiveMultipart()
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            // TODO: makeParkingGreatAgain
                            val dummyOdd = 100
                            call.respondText(Respond.oddRespond(dummyOdd))
                        }
                    }
                }
            }
            post("/history") {
                val multipartData = call.receiveMultipart()
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                        }
                    }
                }
            }
        }
    }.start(wait = true)
}