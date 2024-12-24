package br.com.challenge.backend.feature.fee.repository.cache

import br.com.challenge.backend.feature.fee.domain.Fee
import io.lettuce.core.api.sync.RedisCommands

interface FeeCacheRepository {
    fun getCacheCommands(): RedisCommands<String, String>
    fun loadInterestRates()
    fun getInterestRateByAge(age: Int, commands: RedisCommands<String, String>): Float?
    fun insert(fee: Fee, commands: RedisCommands<String, String>)
}