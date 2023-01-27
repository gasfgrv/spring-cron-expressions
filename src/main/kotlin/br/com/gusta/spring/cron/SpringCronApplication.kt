package br.com.gusta.spring.cron

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringCronApplication

fun main(args: Array<String>) {
	runApplication<SpringCronApplication>(*args)
}
