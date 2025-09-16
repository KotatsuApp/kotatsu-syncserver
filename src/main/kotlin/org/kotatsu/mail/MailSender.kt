package org.kotatsu.mail

interface MailSender {
    suspend fun send(to: String, subject: String, textBody: String, htmlBody: String?)
}
