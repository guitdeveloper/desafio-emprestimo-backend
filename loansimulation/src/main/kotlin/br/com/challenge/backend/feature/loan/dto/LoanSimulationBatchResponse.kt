package br.com.challenge.backend.feature.loan.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoanSimulationBatchResponse(
    val successResult: List<LoanSimulationSuccessResponse>,
    val errorList: List<LoanSimulationErrorResponse>
)