import database.DatabaseFactory
import entities.addNewUser
import entities.addParking
import entities.calculateOdd
import entities.calculateSummary
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.joda.time.DateTime
import utils.Request
import utils.Respond
import utils.ValidationResult
import utils.Validator

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            DatabaseFactory().init()
            embeddedServer(Netty, port = 8006) {
                routing {
                    post("/park") {
                        val token = call.request.headers["auth_token"]
                        val car = Request.parking(call.receiveText())
                        when (Validator.validate(car.carNumber, token)) {
                            ValidationResult.INV_NUM -> {
                                call.respond(HttpStatusCode(400, Respond.ICN))
                            }
                            ValidationResult.NEW_USER -> {
                                val user = addNewUser(car.carNumber)
                                addParking(user)
                                call.response.status(HttpStatusCode.OK)
                                call.respondText(Respond.parkingTimeWithToken(DateTime.now(), user.token))
                            }
                            ValidationResult.INV_TOKEN -> {
                                call.respond(HttpStatusCode(400, Respond.IUT))
                            }
                            ValidationResult.PARKED -> {
                                call.respond(HttpStatusCode(400, Respond.CAP))
                            }
                            ValidationResult.NOT_PARKED -> {
                                addParking(car.carNumber)
                                call.response.status(HttpStatusCode.OK)
                                call.respondText(Respond.parkingTime(DateTime.now()))
                            }
                        }
                    }
                    post("/unpark") {
                        val token = call.request.headers["auth_token"]
                        val car = Request.parking(call.receiveText())
                        when (Validator.validate(car.carNumber, token)) {
                            ValidationResult.INV_NUM -> {
                                call.respond(HttpStatusCode(400, Respond.ICN))
                            }
                            ValidationResult.NEW_USER -> {
                                call.respond(HttpStatusCode(400, Respond.CNP))
                            }
                            ValidationResult.INV_TOKEN -> {
                                call.respond(HttpStatusCode(400, Respond.IUT))
                            }
                            ValidationResult.NOT_PARKED -> {
                                call.respond(HttpStatusCode(400, Respond.CNP))
                            }
                            ValidationResult.PARKED -> {
                                val sum: Int = calculateSummary(car.carNumber)
                                call.respondText(Respond.summary(sum))
                            }
                        }
                    }
                    post("/pay") {
                        val token = call.request.headers["auth_token"]
                        val payment = Request.payment(call.receiveText())
                        when (Validator.validate(payment.carNumber, token)) {
                            ValidationResult.INV_NUM -> {
                                call.respond(HttpStatusCode(400, Respond.ICN))
                            }
                            ValidationResult.NEW_USER -> {
                                call.respond(HttpStatusCode(400, Respond.CNP))
                            }
                            ValidationResult.INV_TOKEN -> {
                                call.respond(HttpStatusCode(400, Respond.IUT))
                            }
                            ValidationResult.NOT_PARKED -> {
                                call.respond(HttpStatusCode(400, Respond.CNP))
                            }
                            ValidationResult.PARKED -> {
                                val odd = calculateOdd(payment.carNumber, payment.sum)
                                call.respondText(Respond.odd(odd))
                            }
                        }
                    }
                    post("/history") {
                        install(Authentication) {
                            basic("auth-basic") {
                                realm = "Access to the '/history' path"
                                validate {
                                    if (it.name == "" && it.password == "") UserIdPrincipal(it.name)
                                    else null
                                }
                            }
                        }
                    }
                }
            }.start(wait = true)
        }
    }
}
