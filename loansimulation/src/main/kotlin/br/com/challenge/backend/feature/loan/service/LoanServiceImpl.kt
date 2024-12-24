import br.com.challenge.backend.feature.loan.domain.LoanResult
import br.com.challenge.backend.feature.loan.domain.LoanSimulation
import br.com.challenge.backend.feature.loan.dto.LoanSimulationSuccessResponse
import br.com.challenge.backend.common.messaging.MessagingImpl
import br.com.challenge.backend.feature.loan.service.LoanService
import br.com.challenge.backend.common.messaging.Messaging
import br.com.challenge.backend.common.HashRandom
import br.com.challenge.backend.feature.loan.domain.LoanSimulationResult
import br.com.challenge.backend.feature.loan.dto.LoanSimulationRequest
import br.com.challenge.backend.feature.loan.repository.LoanRepository
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.Period
import kotlin.reflect.jvm.jvmName
import br.com.challenge.backend.common.Validation as LoadValidation

class LoanServiceImpl(
    private val messaging: Messaging,
    private val loanRepository: LoanRepository,
) : LoanService {

    private val log = KtorSimpleLogger(LoanServiceImpl::class.jvmName)

    override fun simulateLoan(request: LoanSimulationRequest): Any {
        var result: Any
        val validationErrorList = validateLoanSimulationRequest(request)
        if (validationErrorList.isNotEmpty()) {
            result = mapOf("errors" to validationErrorList)
        } else {
            try {
                result = checkLoanSimulation(request)
            } catch (e: Exception) {
                result = mapOf("errors" to listOf("${e.message}"))
            }
        }
        return result
    }

    override fun simulateLoanToBatch(request: LoanSimulationRequest): LoanResult<LoanSimulationResult, LoanSimulationRequest> {
        var result: LoanResult<LoanSimulationResult, LoanSimulationRequest>
        val validationErrorList = validateLoanSimulationRequest(request)
        if (validationErrorList.isNotEmpty()) {
            result = LoanResult.Failure(validationErrorList, request)
        } else {
            try {
                val simulation = checkLoanSimulation(request)
                result = LoanResult.Success(simulation, request)
            } catch (e: Exception) {
                val errors = listOf("${e.message}")
                result = LoanResult.Failure(errors, request)
            }
        }
        return result
    }

    private fun validateLoanSimulationRequest(request: LoanSimulationRequest): List<String> {
        val list = mutableListOf<String>()
        if (!LoadValidation.isValidMoney(request.loanAmount)) {
            list.add("Loan amount must be a valid money")
        }
        if (!LoadValidation.isValidDate(request.birthDate)) {
            list.add("Invalid birth date format")
        }
        if (!LoadValidation.isPositiveNumber(request.paymentTermInMonths)) {
            list.add("Payment term in months must be positive")
        }
        return list
    }

    private fun checkLoanSimulation(request: LoanSimulationRequest): LoanSimulationResult {
        val loan = LoanSimulation(
            BigDecimal(request.loanAmount),
            LocalDate.parse(request.birthDate),
            request.paymentTermInMonths
        )
        val age = calculateAge(loan.birthDate)
        val annualInterestRate = getAnnualInterestRate(age)
        val simulation = calculateLoanDetails(loan.loanAmount, annualInterestRate, loan.paymentTermInMonths)
        sendNotification(simulation, request)
        return simulation
    }

    private fun calculateAge(birthDate: LocalDate): Int {
        return Period.between(birthDate, LocalDate.now()).years
    }

    private fun getAnnualInterestRate(age: Int): Float {
        if (!LoadValidation.isValidAge(age)) {
            throw Exception("Not is possible contract load with age $age")
        }
        return runBlocking { loanRepository.getAnnualInterestRate(age).tax }
    }

    private fun calculateLoanDetails(
        loanAmount: BigDecimal,
        annualInterestRate: Float,
        paymentTermInMonths: Int
    ): LoanSimulationResult {
        val monthlyInterestRate = annualInterestRate / TOTAL_MONTHS / HUNDRED
        if (monthlyInterestRate == 0f) {
            val monthlyPayment = loanAmount.divide(
                BigDecimal(paymentTermInMonths),
                2,
                RoundingMode.HALF_UP
            )
            val totalPayment = monthlyPayment.multiply(BigDecimal(paymentTermInMonths))
            return LoanSimulationResult(
                totalPayment = totalPayment.toString(),
                monthlyInstallment = monthlyPayment.toString(),
                totalInterest = BigDecimal.ZERO.toString()
            )
        }
        val monthlyRate = BigDecimal.valueOf(monthlyInterestRate.toDouble())
        val numerator = loanAmount.multiply(monthlyRate)
        val denominator = BigDecimal.ONE.subtract(
            BigDecimal.ONE.add(monthlyRate).pow(-paymentTermInMonths, MathContext.DECIMAL128)
        )
        val monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP)
        val totalPayment = monthlyPayment.multiply(BigDecimal(paymentTermInMonths))
        val totalInterest = totalPayment.subtract(loanAmount)
        return LoanSimulationResult(
            totalPayment = totalPayment.setScale(2, RoundingMode.HALF_EVEN).toString(),
            monthlyInstallment = monthlyPayment.setScale(2, RoundingMode.HALF_EVEN).toString(),
            totalInterest = totalInterest.setScale(2, RoundingMode.HALF_EVEN).toString()
        )
    }

    private fun sendNotification(result: LoanSimulationResult, request: LoanSimulationRequest) {
        messaging.send(
            MessagingImpl.TOPIC_SEND_MAIL,
            HashRandom.generateHybridHash(),
            LoanSimulationSuccessResponse(result, request)
        )
    }

    companion object {
        const val TOTAL_MONTHS = 12
        const val HUNDRED = 100
    }
}
