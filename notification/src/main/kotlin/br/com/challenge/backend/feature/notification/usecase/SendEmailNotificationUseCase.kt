package br.com.challenge.backend.feature.notification.usecase

import br.com.challenge.backend.feature.notification.domain.EmailNotification

interface SendEmailNotificationUseCase {
    suspend fun execute(emailNotification: EmailNotification)
}