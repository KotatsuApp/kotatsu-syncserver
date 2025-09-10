package org.kotatsu.plugins

import io.ktor.server.application.*
import org.kotatsu.mail.ConsoleMailSender
import org.kotatsu.mail.MailConfig
import org.kotatsu.mail.SmtpMailSender
import org.kotatsu.mailService

fun Application.configureMail() {
    val useConsole = environment.config.propertyOrNull("kotatsu.mail_provider")?.getString() == "console"

    mailService = if (useConsole) {
        ConsoleMailSender()
    } else {
        val config = MailConfig.fromConfig(environment.config)
        SmtpMailSender(config)
    }
}
