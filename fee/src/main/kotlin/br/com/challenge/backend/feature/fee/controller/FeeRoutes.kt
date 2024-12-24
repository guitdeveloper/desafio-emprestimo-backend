import br.com.challenge.backend.feature.fee.dto.FeeResult
import br.com.challenge.backend.feature.fee.service.FeeService
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.feeRoutes(feeService: FeeService) {
    get("/api/v1/fees/{tax_frequency}", {
        tags = listOf("Fee")
        summary = "Get tax rate"
        description = "Fetches the tax rate based on the frequency and age provided."
        request {
            pathParameter<String>("tax_frequency") {
                required = true
                description = "The frequency of the tax (e.g. annual)"
            }
            queryParameter<String>("age") {
                required = true
                description = "The age of the client"
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "The operation was successful"
                body<FeeResult> {
                    description = "The result of the operation"
                }
            }
            HttpStatusCode.BadRequest to {
                description = "An invalid operation was provided"
            }
        }
    }) {
        val taxFrequency = call.parameters["tax_frequency"]
        val age = call.request.queryParameters["age"]
        val result = feeService.getTax(taxFrequency, age)
        val statusCode = if (result is FeeResult) {
            HttpStatusCode.OK
        } else {
            HttpStatusCode.BadRequest
        }
        call.respond(statusCode, result)
    }
}
