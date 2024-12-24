package br.com.challenge.backend.feature.loan.repository

import br.com.challenge.backend.feature.loan.dto.FeeResult

interface LoanRepository {
    suspend fun getAnnualInterestRate(age: Int): FeeResult
}