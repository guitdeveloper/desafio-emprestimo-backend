package br.com.challenge.backend

import br.com.challenge.backend.common.plugin.configureDocAPI
import br.com.challenge.backend.common.plugin.configureLogging
import br.com.challenge.backend.common.plugin.configureDI
import br.com.challenge.backend.common.plugin.configureErrorHandling
import br.com.challenge.backend.common.plugin.configureSerialization
import br.com.challenge.backend.common.plugin.configureRouting
import br.com.challenge.backend.common.messaging.MessagingImpl
import br.com.challenge.backend.feature.notification.dto.NotificationSuccessResponse
import br.com.challenge.backend.feature.notification.usecase.SendEmailNotificationUseCase
import io.ktor.server.application.Application
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDocAPI()
    configureLogging()
    configureDI()
    configureErrorHandling()
    configureSerialization()
    configureRouting()
    val emailUseCase by inject<SendEmailNotificationUseCase>()
    val bootstrapServer = environment.config.property("ktor.messaging").getString()
    MessagingImpl(bootstrapServer, emailUseCase).receive<NotificationSuccessResponse>(MessagingImpl.TOPIC_SEND_MAIL)
}