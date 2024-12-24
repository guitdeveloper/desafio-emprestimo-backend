package br.com.challenge.backend.common.plugin

import FeeServiceImpl
import br.com.challenge.backend.feature.fee.repository.FeeRepository
import br.com.challenge.backend.feature.fee.repository.FeeRepositoryImpl
import br.com.challenge.backend.feature.fee.repository.cache.FeeCacheRepository
import br.com.challenge.backend.feature.fee.repository.cache.FeeCacheRepositoryImpl
import br.com.challenge.backend.feature.fee.service.FeeService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.lettuce.core.RedisClient
import org.koin.dsl.module
import org.koin.ktor.plugin.KoinIsolated
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    val cacheClient = RedisClient.create(environment.config.property("ktor.cache").getString())
    val koinContext = module {
        single<FeeCacheRepository> { FeeCacheRepositoryImpl(cacheClient) }
        single<FeeRepository> { FeeRepositoryImpl(get()) }
        single<FeeService> { FeeServiceImpl(get()) }
    }
    install(KoinIsolated) {
        slf4jLogger()
        modules(koinContext)
    }
}
