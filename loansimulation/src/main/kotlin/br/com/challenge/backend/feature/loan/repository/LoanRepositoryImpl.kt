package br.com.challenge.backend.feature.loan.repository

import br.com.challenge.backend.feature.loan.dto.FeeResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.jackson.jackson

class LoanRepositoryImpl(
    private val feeApi: String
) : LoanRepository {
    override suspend fun getAnnualInterestRate(age: Int): FeeResult {
        val client = HttpClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        try {
            val response: FeeResult = client.get("${feeApi}annual") {
                parameter("age", age)
            }.body()
            return response
        } catch (e: Exception) {
            throw Exception("Error while fetching tax rate: ${e.message}")
        } finally {
            client.close()
        }
    }
}