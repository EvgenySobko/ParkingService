package utils

import entities.isTokenAlreadyUsed
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

object TokenGenerator {

    fun genToken(): String {
        val n = Random.nextBytes(32)
        val md = MessageDigest.getInstance("MD5")
        var t = BigInteger(1, md.digest(n)).toString(16).padStart(32, '0')
        while (isTokenAlreadyUsed(t)) t = genToken()
        return t
    }
}