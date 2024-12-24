import br.com.challenge.backend.feature.fee.dto.FeeResult
import br.com.challenge.backend.feature.fee.repository.FeeRepository
import br.com.challenge.backend.feature.fee.service.FeeService
import io.ktor.util.logging.KtorSimpleLogger
import java.util.Locale
import kotlin.reflect.jvm.jvmName

class FeeServiceImpl(
    private val feeRepository: FeeRepository
) : FeeService {

    private val log = KtorSimpleLogger(FeeServiceImpl::class.jvmName)

    override fun getTax(taxFrequency: String?, age: String?): Any {
        var result: Any
        val validationErrorList = validateParameters(taxFrequency, age)
        result = if (validationErrorList.isNotEmpty()) {
            mapOf("errors" to validationErrorList)
        } else {
            try {
                val taxFrequencyValue = taxFrequency?.trim()?.lowercase(Locale.getDefault()) ?: ""
                val ageValue = age?.toIntOrNull() ?: 0
                checkTax(taxFrequencyValue, ageValue)
            } catch (e: Exception) {
                mapOf("errors" to listOf("${e.message}"))
            }
        }
        return result
    }

    private fun validateParameters(taxFrequency: String?, age: String?): List<String> {
        val list = mutableListOf<String>()
        if (taxFrequency == null) {
            list.add("Missing required parameter: tax_frequency")
        }
        if (age == null) {
            list.add("Missing required parameter: age")
        }
        val taxFrequencyValue = taxFrequency?.trim()?.lowercase(Locale.getDefault())
        if (taxFrequencyValue != TAX_FREQUENCY_ANNUAL) {
            list.add("Tax frequency must be '$TAX_FREQUENCY_ANNUAL'")
        }
        val ageValue = age?.toIntOrNull() ?: 0
        if (ageValue <= 0) {
            list.add("Age must be a positive number")
        }
        return list
    }

    private fun checkTax(taxFrequency: String, age: Int): FeeResult {
        val tax = if (taxFrequency == TAX_FREQUENCY_ANNUAL) {
            feeRepository.getAnnualInterestRate(age)
        } else 0f
        return FeeResult(tax)
    }

    companion object {
        const val TAX_FREQUENCY_ANNUAL = "annual"
    }
}
