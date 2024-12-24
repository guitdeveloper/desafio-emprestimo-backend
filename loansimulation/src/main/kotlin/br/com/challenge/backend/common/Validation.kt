package br.com.challenge.backend.common

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeParseException

object Validation {
    const val MIN_AGE_CONTRACT_LOAN = 18

    fun isValidMoney(value: String): Boolean {
        val money = value.toBigDecimalOrNull()
        return money != null && money > BigDecimal.ZERO
    }

    fun isPositiveNumber(value: Int) = value > 0

    fun isValidDate(value: String): Boolean {
        return try {
            LocalDate.parse(value)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun isValidAge(value: Int) = value >= MIN_AGE_CONTRACT_LOAN
}
