package br.com.challenge.backend.common.plugin

import br.com.challenge.backend.feature.fee.service.FeeService
import feeRoutes
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val feeService by inject<FeeService>()
    routing {
        route("api.json") {
            openApiSpec()
        }
        route("swagger") {
            swaggerUI("/api.json")
        }
        feeRoutes(feeService)
    }
}
