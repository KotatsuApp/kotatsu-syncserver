package org.kotatsu.mail

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*

class SmtpMailSender(private val config: MailConfig): MailSender {
	private val session: Session by lazy {
		val props = Properties().apply {
			put("mail.smtp.auth", "true")
			put("mail.smtp.starttls.enable", "true")
			put("mail.smtp.host", config.host)
			put("mail.smtp.port", config.port)
		}

		Session.getInstance(props, object : Authenticator() {
			override fun getPasswordAuthentication(): PasswordAuthentication {
				return PasswordAuthentication(config.username, config.password)
			}
		})
	}

	override fun send(to: String, subject: String, body: String) {
		try {
			val message = MimeMessage(session).apply {
				setFrom(InternetAddress(config.from))
				setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
				setSubject(subject)
				setText(body)
			}

			Transport.send(message)
			println("Email sent successfully to $to")
		} catch (e: MessagingException) {
			println("Failed to send email: ${e.message}")
			e.printStackTrace()
		}
	}
}
