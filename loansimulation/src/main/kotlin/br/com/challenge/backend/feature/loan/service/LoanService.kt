package br.com.challenge.backend.feature.loan.service

import br.com.challenge.backend.feature.loan.dto.LoanSimulationRequest
import br.com.challenge.backend.feature.loan.domain.LoanSimulationResult
import br.com.challenge.backend.feature.loan.domain.LoanResult

interface LoanService {
    fun simulateLoan(request: LoanSimulationRequest): Any
    fun simulateLoanToBatch(request: LoanSimulationRequest): LoanResult<LoanSimulationResult, LoanSimulationRequest>
}
