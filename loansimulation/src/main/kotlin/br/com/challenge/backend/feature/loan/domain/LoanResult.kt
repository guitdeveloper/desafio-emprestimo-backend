package br.com.challenge.backend.feature.loan.domain

sealed class LoanResult<out T, out O> {
    data class Success<T, O>(val value: T, val owner: O): LoanResult<T, O>()
    data class Failure<O>(val errors: List<String>, val owner: O): LoanResult<Nothing, O>()

    companion object {
        fun <T, O> success(value: T, owner: O): Success<T, O> = Success(value, owner)
        fun <O> failure(errors: List<String>, owner: O): Failure<O> = Failure(errors, owner)
    }
}