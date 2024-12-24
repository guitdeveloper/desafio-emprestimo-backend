package br.com.challenge.backend.feature.loan.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeeResult(
    val tax: Float,
)