package br.com.challenge.backend.common.messaging

interface Messaging {
    fun <T> send(topic: String, key: String, value: T)
}