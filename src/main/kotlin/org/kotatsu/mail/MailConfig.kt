package org.kotatsu.mail

import io.ktor.server.config.*

data class MailConfig(
	val host: String,
	val port: Int,
	val username: String,
	val password: String,
	val from: String
) {
	companion object {
		fun fromConfig(config: ApplicationConfig): MailConfig {
			val smtp = config.config("smtp")
			return MailConfig(
				host = smtp.property("host").getString(),
				port = smtp.property("port").getString().toInt(),
				username = smtp.property("username").getString(),
				password = smtp.property("password").getString(),
				from = smtp.property("sender").getString()
			)
		}
	}
}
