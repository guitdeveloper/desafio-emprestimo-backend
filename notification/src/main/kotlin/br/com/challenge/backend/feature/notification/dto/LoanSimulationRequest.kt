package br.com.challenge.backend.feature.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoanSimulationRequest(
    val loanAmount: String,
    val birthDate: String,
    val paymentTermInMonths: Int
)
