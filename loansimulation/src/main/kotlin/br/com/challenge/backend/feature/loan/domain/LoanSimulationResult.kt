package br.com.challenge.backend.feature.loan.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoanSimulationResult(
    val totalPayment: String,
    val monthlyInstallment: String,
    val totalInterest: String
)
