package br.com.challenge.backend.common

import java.security.MessageDigest
import java.util.*

object HashRandom {
    fun generateHybridHash(): String {
        val randomUUID = UUID.randomUUID().toString()
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(randomUUID.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}