package org.kotatsu.mail

class ConsoleMailSender: MailSender {
    override fun send(to: String, subject: String, body: String) {
        println("=== ConsoleMail ===")
        println("To: $to")
        println("Subject: $subject")
        println("Body (plain):\n$body")
        println("==================")
    }
}
