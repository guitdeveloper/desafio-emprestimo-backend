package br.com.challenge.backend

import br.com.challenge.backend.common.plugin.configureDocAPI
import br.com.challenge.backend.common.plugin.configureLogging
import br.com.challenge.backend.common.plugin.configureDI
import br.com.challenge.backend.common.plugin.configureErrorHandling
import br.com.challenge.backend.common.plugin.configureSerialization
import br.com.challenge.backend.common.plugin.configureRouting
import br.com.challenge.backend.common.plugin.configureExposed
import io.ktor.server.application.Application

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureDocAPI()
    configureLogging()
    configureDI()
    configureErrorHandling()
    configureSerialization()
    configureRouting()
    configureExposed()
}