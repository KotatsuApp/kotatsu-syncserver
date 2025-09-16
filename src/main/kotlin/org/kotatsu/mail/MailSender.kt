package org.kotatsu.mail

interface MailSender {
    fun send(to: String, subject: String, textBody: String, htmlBody: String?)
}
