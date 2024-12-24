package br.com.challenge.backend.feature.loan.dto

import br.com.challenge.backend.feature.loan.domain.LoanSimulationResult
import kotlinx.serialization.Serializable

@Serializable
data class LoanSimulationSuccessResponse(
    val success: LoanSimulationResult,
    val request: LoanSimulationRequest
)