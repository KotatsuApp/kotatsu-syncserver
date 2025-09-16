package org.kotatsu.mail

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.*
import java.util.*

class SmtpMailSender(private val config: SmtpConfig) : MailSender {
    private val session: Session by lazy {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.host", config.host)
            put("mail.smtp.port", config.port)
            if (config.port == 465) {
                put("mail.smtp.ssl.enable", "true")
            } else {
                put("mail.smtp.starttls.enable", "true")
            }
        }

        Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(config.username, config.password)
            }
        })
    }

    override suspend fun send(to: String, subject: String, textBody: String, htmlBody: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(config.from))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                    setSubject(subject)
                    if (htmlBody != null) {
                        val multipart = jakarta.mail.internet.MimeMultipart("alternative")

                        val textPart = jakarta.mail.internet.MimeBodyPart().apply {
                            setText(textBody, "utf-8")
                        }
                        val htmlPart = jakarta.mail.internet.MimeBodyPart().apply {
                            setContent(htmlBody, "text/html; charset=utf-8")
                        }

                        multipart.addBodyPart(textPart)
                        multipart.addBodyPart(htmlPart)

                        setContent(multipart)
                    } else {
                        setText(textBody)
                    }
                }

                Transport.send(message)

                println("Email sent successfully to $to")
            } catch (e: MessagingException) {
                println("Failed to send email: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

