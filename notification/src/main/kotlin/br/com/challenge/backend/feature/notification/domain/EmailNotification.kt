package br.com.challenge.backend.feature.notification.domain

data class EmailNotification(
    val to: String,
    val subject: String,
    val message: String
)
