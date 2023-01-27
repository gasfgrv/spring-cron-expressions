package br.com.gusta.spring.cron.consumer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class TimestampConsumer {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(TimestampConsumer::class.java)
    }

    @KafkaListener(topics = ["\${app.topic}"], groupId = "cron")
    fun consumeTimestamp(@Headers headers: MessageHeaders, @Payload payload: String) {
        LOGGER.info("Topic: {}", headers["kafka_receivedTopic"])
        LOGGER.info("Payload: {}", payload)
    }

}