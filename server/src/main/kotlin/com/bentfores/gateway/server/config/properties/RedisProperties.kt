//package com.bentfores.gateway.server.config.properties
//
//import org.springframework.boot.context.properties.ConfigurationProperties
//
//@ConfigurationProperties(prefix = "spring.cache")
//data class RedisProperties(
//
//  var specs: Map<String, CacheSpec> = mapOf()
//
//) {
//  data class CacheSpec(
//    var expireTimeSeconds: Long = 60,
//    var methodCachingEnabled: Boolean = false
//  )
//}