package br.com.gusta.spring.cron.producer

import java.util.concurrent.TimeUnit
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName


@Testcontainers
@SpringBootTest
@ExtendWith(OutputCaptureExtension::class)
class TimestampProducerTest {

    companion object {

        @Container
        private val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers") { kafkaContainer.bootstrapServers }
        }

    }

    @SpyBean
    private lateinit var timestampProducer: TimestampProducer

    @Test
    fun `Must send 6 messages in 30 seconds to kafka`() {
        await()
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted { verify(timestampProducer, atLeast(6)).sendTimestamp() }
    }

}
