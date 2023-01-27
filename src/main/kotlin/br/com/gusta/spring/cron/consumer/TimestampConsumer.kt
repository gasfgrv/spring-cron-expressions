package br.com.gusta.spring.cron.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TimestampConsumer {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(TimestampConsumer::class.java)
    }

    @KafkaListener(topics = ["\${app.topic}"], groupId = "cron")
    fun consumeTimestamp(payload: ConsumerRecord<String, String>) {
        LOGGER.info("Payload: {}", payload.value())
    }

}