package br.com.challenge.backend.common.messaging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

class MessagingImpl(
    val bootstrapServers: String
) : Messaging {
    override fun <T> send(topic: String, key: String, value: T) {
        CoroutineScope(Dispatchers.IO).launch {
            val producerProperties = mapOf(
                "bootstrap.servers" to bootstrapServers,
                "key.serializer" to StringSerializer::class.java.name,
                "value.serializer" to StringSerializer::class.java.name
            )
            val producer = KafkaProducer<String, String>(producerProperties)
            val jsonResult = jacksonObjectMapper().writeValueAsString(value)
            val record = ProducerRecord(topic, key, jsonResult)
            producer.use {
                it.send(record)
                println("Simulation result sent: $jsonResult")
            }
        }
    }

    companion object {
        const val TOPIC_SEND_MAIL = "send-simulation-result-by-mail"
    }
}
