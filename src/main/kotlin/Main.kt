
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import database.DatabaseFactory
import entities.addParking
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
import utils.DateTimeUtil
import utils.Respond
import utils.Validator

class Main {

    companion object {

        private val environment = applicationEngineEnvironment {  }

        private val jwtIssuer = environment.config.property("jwt.domain").getString()
        private val jwtAudience = environment.config.property("jwt.audience").getString()
        private val jwtRealm = environment.config.property("jwt.realm").getString()

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
                install(Authentication) {
                    jwt {
                        realm = jwtRealm
                        verifier(makeJwtVerifier(jwtIssuer, jwtAudience))
                        validate { credential ->
                            if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
                        }
                    }
                }
                routing {
                    post("/park") {
                        val carNumber: String = Respond.parkingRequest(call.receiveText())
                        if (Validator.validateCarNumber(carNumber)) {
                            if (isCarParkedNow(carNumber)) call.respond(HttpStatusCode(400, Respond.CAP))
                            else {
                                addParking(carNumber)
                                call.response.status(HttpStatusCode.OK)
                                call.respondText(Respond.parkingTimeRespond(DateTimeUtil.getCurrentDateAndTime()))
                            }
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
