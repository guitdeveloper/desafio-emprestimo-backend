package br.com.challenge.backend.feature.notification.usecase

import br.com.challenge.backend.feature.notification.domain.EmailNotification
import br.com.challenge.backend.feature.notification.gateway.EmailGateway

class SendEmailNotificationUseCaseImpl(
    private val emailGateway: EmailGateway
) : SendEmailNotificationUseCase {
    override suspend fun execute(emailNotification: EmailNotification) {
        emailGateway.sendEmail(emailNotification)
    }
}