package br.com.challenge.backend.feature.fee.service

interface FeeService {
    fun getTax(taxFrequency: String?, age: String?): Any
}
