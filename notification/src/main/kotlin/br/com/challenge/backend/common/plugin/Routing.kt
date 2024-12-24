package br.com.challenge.backend.common.plugin

import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.routing.route

fun Application.configureRouting() {
    routing {
        route("api.json") {
            openApiSpec()
        }
        route("swagger") {
            swaggerUI("/api.json")
        }
    }
}
