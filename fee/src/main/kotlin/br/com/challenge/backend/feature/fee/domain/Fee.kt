package br.com.challenge.backend.feature.fee.domain

data class Fee(
    val rate: Float,
    val ageStart: Int,
    val ageEnd: Int
)