package br.com.challenge.backend.common.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.calllogging.processingTimeMillis
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val status = call.response.status()
            val time = call.processingTimeMillis()
            "$httpMethod $path --> $status @${time}ms"
        }
    }
    monitor.subscribe(ApplicationStarted) { application ->
        val env = application.environment.config.property("ktor.environment").getString()
        application.environment.log.info("Application is started on '$env' environment")
    }
    monitor.subscribe(ApplicationStopped) { application ->
        application.environment.log.info("Application is stopped")
        monitor.unsubscribe(ApplicationStarted) {}
    }
}
