package utils

import io.ktor.network.tls.certificates.*
import io.ktor.network.tls.extensions.*
import io.ktor.server.engine.*
import java.io.File
import java.security.KeyStore

object CertificateGenerator {

    fun ApplicationEngineEnvironmentBuilder.installCertificates() {
        val keystore = buildKeyStore {
            certificate(ALIAS) {
                hash = HashAlgorithm.SHA256
                sign = SignatureAlgorithm.ECDSA
                keySizeInBits = 256
                password = PASS
            }
        }
        sslConnector(keystore, ALIAS, { "".toCharArray() }, { PASS.toCharArray() }) {
            port = 8007
            keyStorePath = keystore.asFile.absoluteFile
        }
    }

    private const val ALIAS = "cert"
    private const val PASS = "12345"

    private val KeyStore.asFile: File
        get() {
            val keyStoreFile = File("build/cert.jks")
            this.saveToFile(keyStoreFile, PASS)
            return keyStoreFile
        }


    /**
     * This one is just for creating certificates
     * **/
    @JvmStatic
    fun main(args: Array<String>) {
        val jksFile = File("build/cert.jks").apply {
            parentFile.mkdirs()
        }

        if (!jksFile.exists()) {
            generateCertificate(jksFile) // Generates the certificate
        }
    }
}