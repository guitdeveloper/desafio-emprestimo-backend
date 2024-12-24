package br.com.challenge.backend.feature.loan.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoanSimulationErrorResponse(
    val errors: List<String>,
    val request: LoanSimulationRequest
)