package br.com.challenge.backend.feature.fee.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeeResult(
    val tax: Float
)
