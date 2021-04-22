import database.DatabaseFactory
import entities.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.joda.time.DateTime
import utils.*

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            DatabaseFactory().init()
            embeddedServer(Netty, port = 8006) {
                routing {
                    post("/park") {
                        val carNumber: String = Respond.parkingRequest(call.receiveText())
                        if (Validator.validateCarNumber(carNumber)) {
                            if (isCarParkedNow(carNumber))
                                call.respond(HttpStatusCode(400, Respond.CAP))
                            else {
                                addParking(carNumber)
                                call.response.status(HttpStatusCode.OK)
                                call.respondText(Respond.parkingTimeRespond(DateTime.now()))
                            }
                        } else {
                            call.respond(HttpStatusCode(400, Respond.ICN))
                        }
                    }
                    post("/unpark") {
                        val carNumber: String = Respond.parkingRequest(call.receiveText())
                        println(carNumber)
                        if (Validator.validateCarNumber(carNumber)) {
                            if (!isCarParkedNow(carNumber))
                                call.respond(HttpStatusCode(400, Respond.CNP))
                            else {
                                val sum: Int = calculateSummary(carNumber)
                                call.respondText(Respond.summaryRespond(sum))
                            }
                        } else {
                            call.respond(HttpStatusCode(400, Respond.ICN))
                        }

                    }
                    post("/pay") {
                        val payment = Respond.paymentRequest(call.receiveText())
                        println(payment)
                        if (Validator.validateCarNumber(payment.carNumber)) {
                            if (!isCarParkedNow(payment.carNumber))
                                call.respond(HttpStatusCode(400, Respond.CNP))
                            else {
                                val odd = calculateOdd(payment.carNumber, payment.sum)
                                call.respondText(Respond.oddRespond(odd))
                            }
                        }
                    }
                    post("/history") {

                    }
                }
            }.start(wait = true)
        }
    }
}
