package br.com.challenge.backend.feature.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoanSimulationResult(
    val totalPayment: String,
    val monthlyInstallment: String,
    val totalInterest: String
)
