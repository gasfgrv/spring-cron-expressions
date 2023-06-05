package br.com.gusta.spring.cron.consumer

import java.time.Clock
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName


@Testcontainers
@SpringBootTest
@ExtendWith(OutputCaptureExtension::class)
class TimestampConsumerTest {

    companion object {

        @Container
        private val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers") { kafkaContainer.bootstrapServers }
        }

    }

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var timestampConsumer: TimestampConsumer

    @Value("\${app.topic}")
    private lateinit var topic: String

    @Mock
    private lateinit var clock: Clock

    @Test
    fun `Must read a message written in the topic`(output: CapturedOutput) {
        val mockNow = ZonedDateTime.of(
            LocalDateTime.of(2023, 1, 1, 10, 30),
            ZoneId.of("Z")
        )

        Mockito.`when`(clock.zone).thenReturn(mockNow.zone)
        Mockito.`when`(clock.instant()).thenReturn(mockNow.toInstant())

        val timestamp = OffsetDateTime.now(clock).toString()
        val mensagem = MessageBuilder.withPayload(timestamp)
            .setHeader(KafkaHeaders.TOPIC, topic)
            .setHeader(KafkaHeaders.RECEIVED_TOPIC, topic)
            .build()

        kafkaTemplate.send(mensagem)

        timestampConsumer.consumeTimestamp(mensagem.headers, mensagem.payload)

        assertThat(output.out).contains("Topic: $topic")
        assertThat(output.out).contains(mockNow.toString())
    }

}
