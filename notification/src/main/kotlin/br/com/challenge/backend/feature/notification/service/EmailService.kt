package br.com.challenge.backend.feature.notification.service

interface EmailService {
    suspend fun send(to: String, subject: String, message: String)
}