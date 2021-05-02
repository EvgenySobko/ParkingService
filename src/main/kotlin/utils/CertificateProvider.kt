package utils

import io.ktor.network.tls.certificates.*
import io.ktor.network.tls.extensions.*
import io.ktor.server.engine.*
import java.io.File
import java.security.KeyStore

object CertificateProvider {

    fun ApplicationEngineEnvironmentBuilder.installCertificates() {
        val keystore = buildKeyStore {
            certificate(ALIAS) {
                hash = HashAlgorithm.SHA256
                sign = SignatureAlgorithm.ECDSA
                keySizeInBits = 256
                password = PASS
            }
        }
        sslConnector(keystore, ALIAS, { PASS.toCharArray() }, { PASS.toCharArray() }) {
            port = 8007
            keyStorePath = keystore.asFile.absoluteFile
        }
    }

    private val KeyStore.asFile: File
        get() {
            val keyStoreFile = File(KEYSTORE)
            this.saveToFile(keyStoreFile, PASS)
            return keyStoreFile
        }

    private const val ALIAS = "myalias"
    private const val PASS = "mypass"
    private const val KEYSTORE = "/etc/letsencrypt/live/bulochka.duckdns.org/keystore.jks"
}