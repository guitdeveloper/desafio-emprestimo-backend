package br.com.challenge.backend.common.plugin

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.SwaggerUiSort
import io.github.smiley4.ktorswaggerui.data.SwaggerUiSyntaxHighlight
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureDocAPI() {
    install(SwaggerUI) {
        swagger {
            displayOperationId = false
            showTagFilterInput = false
            sort = SwaggerUiSort.NONE
            syntaxHighlight = SwaggerUiSyntaxHighlight.MONOKAI
            withCredentials = false
        }
        info {
            title = "Fee"
            version = "latest"
            description = "Example API for find taxes."
        }
        server {
            url = "http://localhost:" + environment.config.property("ktor.deployment.port").getString()
            description = "Development Server"
        }
    }
}
