package org.kotatsu.mail

import io.ktor.server.config.*

data class SmtpConfig(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val from: String,
) {
    companion object {
        fun fromConfig(config: ApplicationConfig): SmtpConfig {
            val smtp = config.config("smtp")

            val host = smtp.propertyOrNull("host")?.getString()
            val port = smtp.propertyOrNull("port")?.getString()?.toInt()
            val username = smtp.propertyOrNull("username")?.getString()
            val password = smtp.propertyOrNull("password")?.getString()
            val from = smtp.propertyOrNull("from")?.getString()

            require(!host.isNullOrBlank()) { "SMTP host must be set when mail_provider is smtp" }
            require(port != null) { "SMTP port must be set when mail_provider is smtp" }
            require(!username.isNullOrBlank()) { "SMTP username must be set when mail_provider is smtp" }
            require(!password.isNullOrBlank()) { "SMTP password must be set when mail_provider is smtp" }
            require(!from.isNullOrBlank()) { "SMTP from must be set when mail_provider is smtp" }

            return SmtpConfig(host, port, username, password, from)
        }
    }
}
