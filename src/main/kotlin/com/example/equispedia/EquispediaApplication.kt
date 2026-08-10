package com.example.equispedia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class EquispediaApplication

fun main(args: Array<String>) {
	runApplication<EquispediaApplication>(*args)
}
