package org.kotatsu.mail

class ConsoleMailSender: MailSender {
    override fun send(to: String, subject: String, textBody: String, htmlBody: String?) {
        println("=== ConsoleMail ===")
        println("To: $to")
        println("Subject: $subject")
        println("Body (plain):\n$textBody")
        println("==================")
    }
}
