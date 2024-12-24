import br.com.challenge.backend.feature.loan.domain.LoanResult
import br.com.challenge.backend.feature.loan.domain.LoanSimulationResult
import br.com.challenge.backend.feature.loan.dto.LoanSimulationBatchResponse
import br.com.challenge.backend.feature.loan.dto.LoanSimulationRequest
import br.com.challenge.backend.feature.loan.dto.LoanSimulationErrorResponse
import br.com.challenge.backend.feature.loan.dto.LoanSimulationSuccessResponse
import br.com.challenge.backend.feature.loan.service.LoanService
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.coroutines.*

fun Route.loanSimulationRoutes(loanService: LoanService) {
    post("/api/v1/loans/simulations", {
        tags = listOf("One Simulation")
        description = "Simulate one loan amount"
        request {
            body<LoanSimulationRequest>()
        }
        response {
            HttpStatusCode.OK to {
                description = "The operation was successful"
                body<LoanSimulationResult> {
                    description = "The result of the operation"
                }
            }
            HttpStatusCode.BadRequest to {
                description = "An invalid operation was provided"
            }
        }
    }) {
        val request = try {
            call.receive<LoanSimulationRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid request body - ${e.message}"))
            return@post
        }
        val result = loanService.simulateLoan(request)
        val statusCode = if (result is LoanSimulationResult) {
            HttpStatusCode.OK
        } else {
            HttpStatusCode.BadRequest
        }
        call.respond(statusCode, result)
    }
    post("/api/v1/loans/simulations/batch", {
        tags = listOf("Batch Simulation")
        description = "Simulate batch loan amount"
        request {
            body<List<LoanSimulationRequest>>()
        }
        response {
            HttpStatusCode.OK to {
                description = "The operation was successful"
                body<LoanSimulationBatchResponse> {
                    description = "The result of the operation"
                }
            }
            HttpStatusCode.BadRequest to {
                description = "An invalid operation was provided"
            }
        }
    }) {
        val requests = try {
            call.receive<List<LoanSimulationRequest>>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid request body - ${e.message}"))
            return@post
        }
        val resultList = coroutineScope {
            requests.map { request ->
                async {
                    loanService.simulateLoanToBatch(request)
                }
            }.awaitAll()
        }
        val successList = resultList.filterIsInstance<LoanResult.Success<LoanSimulationResult, LoanSimulationRequest>>()
            .map {
                LoanSimulationSuccessResponse(
                    success = it.value,
                    request = it.owner
                )
            }
        val errorList = resultList.filterIsInstance<LoanResult.Failure<LoanSimulationRequest>>()
            .map {
                LoanSimulationErrorResponse(
                    errors = it.errors,
                    request = it.owner
                )
            }
        val results = LoanSimulationBatchResponse(successList, errorList)
        call.respond(HttpStatusCode.OK, results)
    }
}
