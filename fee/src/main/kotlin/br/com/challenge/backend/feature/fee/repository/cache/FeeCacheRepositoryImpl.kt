package br.com.challenge.backend.feature.fee.repository.cache

import br.com.challenge.backend.feature.fee.domain.Fee
import br.com.challenge.backend.feature.fee.entity.InterestRate
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class FeeCacheRepositoryImpl(
    private val cacheClient: RedisClient
) : FeeCacheRepository {

    override fun getCacheCommands(): RedisCommands<String, String> {
        val connection = cacheClient.connect()
        return connection.sync()
    }

    override fun loadInterestRates() {
        val connection = getConnection()
        val commands = connection.sync()
        transaction {
            InterestRate.selectAll().forEach {
                val fee = Fee(
                    it[InterestRate.interestRate].toFloat(),
                    it[InterestRate.ageRangeStart],
                    it[InterestRate.ageRangeEnd]
                )
                insert(fee, commands)
            }
        }
        connection.close()
        cacheClient.shutdown()
    }

    override fun getInterestRateByAge(
        age: Int,
        commands: RedisCommands<String, String>
    ): Float? {
        val keys = commands.keys("$PREFIX_KEY_FEE*")
        for (key in keys) {
            val value = commands[key] ?: continue
            val rateCache = jacksonObjectMapper().readValue(value, Fee::class.java)
            if (age in rateCache.ageStart..rateCache.ageEnd) {
                return rateCache.rate
            }
        }
        return null
    }

    override fun insert(fee: Fee, commands: RedisCommands<String, String>) {
        val key = "$PREFIX_KEY_FEE${fee.ageStart}:${fee.ageEnd}"
        val value = jacksonObjectMapper().writeValueAsString(fee)
        commands.setex(key, EXPIRATION_SECONDS_BY_DAY, value)
    }

    private fun getConnection(): StatefulRedisConnection<String, String> {
        return cacheClient.connect()
    }

    companion object {
        const val PREFIX_KEY_FEE = "interest_rate:age_range:"
        const val EXPIRATION_SECONDS_BY_DAY = 60L * 24
    }
}