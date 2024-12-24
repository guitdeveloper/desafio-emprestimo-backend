package br.com.challenge.backend.feature.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSuccessResponse(
    val success: LoanSimulationResult,
    val request: LoanSimulationRequest
)