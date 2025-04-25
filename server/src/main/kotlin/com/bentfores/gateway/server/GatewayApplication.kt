package com.bentfores.gateway.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@EnableCaching
class GatewayApplication

fun main(args: Array<String>) {
	runApplication<GatewayApplication>(*args)
}