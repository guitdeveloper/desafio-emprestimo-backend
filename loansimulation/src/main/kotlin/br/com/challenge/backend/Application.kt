package br.com.challenge.backend

import br.com.challenge.backend.feature.loan.dto.LoanSimulationRequest
import br.com.challenge.backend.common.plugin.configureDocAPI
import br.com.challenge.backend.common.plugin.configureLogging
import br.com.challenge.backend.common.plugin.configureDI
import br.com.challenge.backend.common.plugin.configureErrorHandling
import br.com.challenge.backend.common.plugin.configureSerialization
import br.com.challenge.backend.common.plugin.configureRouting
import io.ktor.server.application.Application
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    generateSimulations()
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDocAPI()
    configureLogging()
    configureDI()
    configureErrorHandling()
    configureSerialization()
    configureRouting()
}

fun generateSimulations() {
    val simulations = (1..10_000).map {
        LoanSimulationRequest(
            loanAmount = (1000..500000).random().toString(),
            birthDate = "${(1960..2003).random()}-${(1..12).random().toString().padStart(2, '0')}-${(1..28).random().toString().padStart(2, '0')}",
            paymentTermInMonths = (12..72).random()
        )
    }
    val payload = Json.encodeToString(simulations)
    return
}