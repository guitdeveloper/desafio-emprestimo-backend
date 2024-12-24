package br.com.challenge.backend.feature.notification.adapter

import br.com.challenge.backend.feature.notification.domain.EmailNotification
import br.com.challenge.backend.feature.notification.gateway.EmailGateway
import br.com.challenge.backend.feature.notification.service.EmailService

class EmailGatewayImpl(
    private val emailService: EmailService
) : EmailGateway {
    override suspend fun sendEmail(emailNotification: EmailNotification) {
        emailService.send(
            to = emailNotification.to,
            subject = emailNotification.subject,
            message = emailNotification.message
        )
    }
}