package br.com.challenge.backend.feature.notification.gateway

import br.com.challenge.backend.feature.notification.domain.EmailNotification

interface EmailGateway {
    suspend fun sendEmail(emailNotification: EmailNotification)
}