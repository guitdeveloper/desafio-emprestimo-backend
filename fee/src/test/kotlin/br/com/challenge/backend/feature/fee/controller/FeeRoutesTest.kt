package br.com.challenge.backend.feature.fee.controller

import br.com.challenge.backend.feature.fee.dto.FeeResult
import br.com.challenge.backend.feature.fee.service.FeeService
import feeRoutes
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class FeeRoutesTest {

    private val feeService = mockk<FeeService>()

    @Test
    fun `should return 200 with valid tax frequency and age`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.yaml")
        }
        val feeResult = FeeResult(0.05f)
        every { feeService.getTax("annual", "30") } returns feeResult
        application {
            routing {
                feeRoutes(feeService)
            }
        }

        val response = client.get("/api/v1/fees/annual?age=30")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"tax":0.05}""", response.bodyAsText())
        verify { feeService.getTax("annual", "30") }
    }
}