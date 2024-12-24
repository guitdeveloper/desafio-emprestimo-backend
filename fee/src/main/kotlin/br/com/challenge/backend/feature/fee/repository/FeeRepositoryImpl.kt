package br.com.challenge.backend.feature.fee.repository

import br.com.challenge.backend.feature.fee.domain.Fee
import br.com.challenge.backend.feature.fee.entity.InterestRate
import br.com.challenge.backend.feature.fee.repository.cache.FeeCacheRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class FeeRepositoryImpl(
    private val feeCacheRepository: FeeCacheRepository
) : FeeRepository {
    override fun getAnnualInterestRate(age: Int): Float {
        val commands = feeCacheRepository.getCacheCommands()
        var interestRate = feeCacheRepository.getInterestRateByAge(age, commands)
        if (interestRate == null) {
            transaction {
                val fee = InterestRate
                    .selectAll()
                    .where {
                        InterestRate.interestRateType eq "annual" and
                                (InterestRate.ageRangeStart lessEq age) and
                                (InterestRate.ageRangeEnd greaterEq age) and
                                (InterestRate.effectiveDateEnd.isNull())
                    }
                    .map {
                        it.toFee()
                    }
                    .singleOrNull()
                fee?.let {
                    interestRate = it.rate
                    feeCacheRepository.insert(it, commands)
                }
            }
        }
        return interestRate ?: 0f
    }

    companion object {
        private fun ResultRow.toFee() = Fee(
            this[InterestRate.interestRate].toFloat(),
            this[InterestRate.ageRangeStart],
            this[InterestRate.ageRangeEnd]
        )
    }
}