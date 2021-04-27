
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import database.DatabaseFactory
import entities.addParking
import entities.calculateOdd
import entities.calculateSummary
import entities.isCarParkedNow
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.joda.time.DateTime
import utils.Respond
import utils.Validator

class Main {

    companion object {

        private val algorithm = Algorithm.HMAC256("secret")

        private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
            .require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

        @JvmStatic
        fun main(args: Array<String>) {
            DatabaseFactory().init()
            embeddedServer(Netty, port = 8006) {
                routing {

                    val jwtIssuer = "https://jwt-provider-domain/"
                    val jwtAudience = "jwt-audience"
                    val jwtRealm = "ParkingService"

                    install(Authentication) {
                        jwt {
                            realm = jwtRealm
                            verifier(makeJwtVerifier(jwtIssuer, jwtAudience))
                            validate { credential ->
                                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
                            }
                        }
                    }

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
                            val odd = calculateOdd(payment.carNumber, payment.sum)
                            call.respondText(Respond.oddRespond(odd))
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
