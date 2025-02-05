package com.bentfores.gateway.server

import com.bentfores.gateway.server.config.properties.RedisProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableConfigurationProperties(RedisProperties::class)
@SpringBootApplication
@EnableCaching
class GatewayApplication

fun main(args: Array<String>) {
	runApplication<GatewayApplication>(*args)
}