package br.com.challenge.backend.common.plugin

import br.com.challenge.backend.feature.notification.adapter.EmailGatewayImpl
import br.com.challenge.backend.feature.notification.gateway.EmailGateway
import br.com.challenge.backend.feature.notification.service.EmailService
import br.com.challenge.backend.feature.notification.service.EmailServiceImpl
import br.com.challenge.backend.feature.notification.usecase.SendEmailNotificationUseCase
import br.com.challenge.backend.feature.notification.usecase.SendEmailNotificationUseCaseImpl
import br.com.challenge.backend.common.messaging.Messaging
import br.com.challenge.backend.common.messaging.MessagingImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.KoinIsolated
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    val bootstrapServers = environment.config.property("ktor.messaging").getString()
    val koinContext = module {
        single<EmailService> { EmailServiceImpl() }
        single<EmailGateway> { EmailGatewayImpl(get()) }
        single<SendEmailNotificationUseCase> { SendEmailNotificationUseCaseImpl(get()) }
        single<Messaging> { MessagingImpl(bootstrapServers, get()) }
    }
    install(KoinIsolated) {
        slf4jLogger()
        modules(koinContext)
    }
}
