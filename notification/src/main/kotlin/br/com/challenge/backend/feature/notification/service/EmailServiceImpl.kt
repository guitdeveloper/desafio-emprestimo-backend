package br.com.challenge.backend.feature.notification.service

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailServiceImpl : EmailService {
    override suspend fun send(to: String, subject: String, message: String) {
        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }
        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("noreply@notification.com", "*******")
            }
        })
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress("noreply@notification.com"))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                setSubject(subject)
                setText(message)
            }
            Transport.send(message)
            println("Email sent to: $to")
        } catch (e: MessagingException) {
            println("Failed to send email: ${e.message}")
        }
    }
}