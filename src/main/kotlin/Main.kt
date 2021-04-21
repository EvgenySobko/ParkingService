
import database.DatabaseFactory
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import utils.DateTime
import utils.Respond
import utils.Validator

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            DatabaseFactory().init()
            embeddedServer(Netty, port = 8006) {
                routing {
                    get("/users") {
                        call.respond("getUsers()")
                    }
                    post("/park") {
                        val carNumber: String = Respond.parkingRequest(call.receiveText())
                        println(carNumber)
                        if (Validator.validateCarNumber(carNumber)) {
                            // TODO: registerParking()
                            call.response.status(HttpStatusCode.OK)
                            call.respondText(Respond.parkingTimeRespond(DateTime.getCurrentDateAndTime()))
                        } else {
                            call.respond(HttpStatusCode(400, Respond.ICN))
                        }


                    }
                    post("/unpark") {
                        val carNumber: String = Respond.parkingRequest(call.receiveText())
                        println(carNumber)
                        if (Validator.validateCarNumber(carNumber)) {
                            // TODO: checkIfCarIsNotParked
                            // TODO: calculateSummary
                            val dummySummary = 1000
                            call.respondText(Respond.summaryRespond(dummySummary))
                        } else {
                            call.respond(HttpStatusCode(400, Respond.ICN))
                        }

                    }
                    post("/pay") {
                        val payment = Respond.paymentRequest(call.receiveText())
                        println(payment)
                        if (Validator.validateCarNumber(payment.carNumber)) {
                            // TODO: calculate odd due to payment.sum
                            val dummyOdd = 100
                            call.respondText(Respond.oddRespond(dummyOdd))
                        }
                    }
                    post("/history") {

                    }
                }
            }.start(wait = true)
        }
    }
}
