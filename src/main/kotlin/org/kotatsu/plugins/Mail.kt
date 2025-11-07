package org.kotatsu.plugins

import io.ktor.server.application.*
import org.kotatsu.mail.ConsoleMailSender
import org.kotatsu.mail.SmtpConfig
import org.kotatsu.mail.SmtpMailSender
import org.kotatsu.mailService

fun Application.configureMail() {
    val provider = environment.config.propertyOrNull("kotatsu.mail_provider")?.getString() ?: "console"

    mailService = when (provider) {
        "smtp" -> SmtpMailSender(SmtpConfig.fromConfig(environment.config))
        else -> ConsoleMailSender()
    }
}
