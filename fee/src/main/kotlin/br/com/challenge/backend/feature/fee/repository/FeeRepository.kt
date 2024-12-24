package br.com.challenge.backend.feature.fee.repository

interface FeeRepository {
    fun getAnnualInterestRate(age: Int): Float
}