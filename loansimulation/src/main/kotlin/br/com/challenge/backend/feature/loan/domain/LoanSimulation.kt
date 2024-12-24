package br.com.challenge.backend.feature.loan.domain

import java.math.BigDecimal
import java.time.LocalDate

data class LoanSimulation(
    val loanAmount: BigDecimal,
    val birthDate: LocalDate,
    val paymentTermInMonths: Int
)
