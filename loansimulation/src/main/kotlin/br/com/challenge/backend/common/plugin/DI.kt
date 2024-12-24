package br.com.challenge.backend.common.plugin

import LoanServiceImpl
import br.com.challenge.backend.feature.loan.service.LoanService
import br.com.challenge.backend.common.messaging.Messaging
import br.com.challenge.backend.common.messaging.MessagingImpl
import br.com.challenge.backend.feature.loan.repository.LoanRepository
import br.com.challenge.backend.feature.loan.repository.LoanRepositoryImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.KoinIsolated
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    val bootstrapServers = environment.config.property("ktor.messaging").getString()
    val feeApi = environment.config.property("ktor.feeApi").getString()
    val koinContext = module {
        single<Messaging> { MessagingImpl(bootstrapServers) }
        single<LoanRepository> { LoanRepositoryImpl(feeApi) }
        single<LoanService> { LoanServiceImpl(get(), get()) }
    }
    install(KoinIsolated) {
        slf4jLogger()
        modules(koinContext)
    }
}
