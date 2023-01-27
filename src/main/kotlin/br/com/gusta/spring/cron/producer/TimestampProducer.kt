package br.com.gusta.spring.cron.producer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class TimestampProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(TimestampProducer::class.java)
    }

    @Value("\${app.topic}")
    lateinit var topic: String

    @Scheduled(cron = "0/5 * * ? * *")
    fun sendTimestamp() {
        val timestamp = OffsetDateTime.now().toString()

        LOGGER.info("Sending {} to kafka", timestamp)

        val message = MessageBuilder.withPayload(timestamp)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build()

        kafkaTemplate.send(message)
    }

}