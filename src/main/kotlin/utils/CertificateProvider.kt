package utils

import io.ktor.server.engine.*
import java.io.File
import java.security.KeyStore

object CertificateProvider {

    fun ApplicationEngineEnvironmentBuilder.installCertificates() {
        val keystore = KeyStore.getInstance(File(KEYSTORE), PASS.toCharArray())
        sslConnector(keystore, ALIAS, { PASS.toCharArray() }, { PASS.toCharArray() }) {
            port = 8007
        }
    }

    private const val ALIAS = "myalias"
    private const val PASS = "mypass"
    private const val KEYSTORE = "/Users/evgenysobko/Desktop/keystore.jks"
}