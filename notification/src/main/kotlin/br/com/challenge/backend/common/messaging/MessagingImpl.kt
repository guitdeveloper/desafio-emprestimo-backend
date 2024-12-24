package br.com.challenge.backend.common.messaging

import br.com.challenge.backend.feature.notification.domain.EmailNotification
import br.com.challenge.backend.feature.notification.dto.NotificationSuccessResponse
import br.com.challenge.backend.feature.notification.usecase.SendEmailNotificationUseCase
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.Properties

class MessagingImpl(
    val bootstrapServers: String,
    private val emailUseCase: SendEmailNotificationUseCase
) : Messaging {
    inline fun <reified T> receive(topic: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val consumerProperties = Properties().apply {
                put("bootstrap.servers", bootstrapServers)
                put("group.id", "simulation-result-group")
                put("key.deserializer", StringDeserializer::class.java.name)
                put("value.deserializer", StringDeserializer::class.java.name)
                put("auto.offset.reset", "earliest")
            }
            val consumer = KafkaConsumer<String, String>(consumerProperties)
            val objectMapper = jacksonObjectMapper()
            consumer.subscribe(listOf(topic))
            while (true) {
                val records = consumer.poll(java.time.Duration.ofMillis(5000))
                for (record in records) {
                    try {
                        val result: T = objectMapper.readValue(record.value(), T::class.java)
                        println("Received simulation result: $result")
                        sendEmailNotification(result as NotificationSuccessResponse)
                    } catch (e: Exception) {
                        println("Failed to process record: ${e.message}")
                    }
                }
            }
        }
    }

    fun sendEmailNotification(post: NotificationSuccessResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            val emailNotification = EmailNotification(
                to = "customer@domain.com",
                subject = "Result from loan simulation",
                message = "Hello! Here is the results from loan simulation: " + Json.encodeToString(post)
            )
            emailUseCase.execute(emailNotification)
        }
    }

    companion object {
        const val TOPIC_SEND_MAIL = "send-simulation-result-by-mail"
    }
}
