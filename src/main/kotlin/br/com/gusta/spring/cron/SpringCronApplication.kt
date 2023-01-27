package br.com.gusta.spring.cron

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.scheduling.annotation.EnableScheduling

@EnableKafka
@EnableScheduling
@SpringBootApplication
class SpringCronApplication

fun main(args: Array<String>) {
	runApplication<SpringCronApplication>(*args)
}
