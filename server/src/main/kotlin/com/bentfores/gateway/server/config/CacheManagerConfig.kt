package com.bentfores.gateway.server.config

import com.bentfores.gateway.server.config.properties.RedisProperties
import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext

@Configuration
class CacheManagerConfig(
  @Value("\${spring.cache.enabled}")
  private val isCacheEnabled: Boolean,
  private val cacheProperties: RedisProperties
) {

  @Bean
  fun redisCacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
    if (!isCacheEnabled) {
      return RedisCacheManager.builder(connectionFactory)
        .disableCreateOnMissingCache()
        .build()
    }

    val cacheConfigurations: MutableMap<String, RedisCacheConfiguration> = HashMap()

    cacheProperties.specs.forEach { (cacheName, config) ->
      if (config.methodCachingEnabled) {

        val cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofSeconds(config.expireTimeSeconds))
          .disableCachingNullValues()
          .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
              GenericJackson2JsonRedisSerializer()
            )
          )

        cacheConfigurations[cacheName] = cacheConfig
      }
    }

    return RedisCacheManager.builder(connectionFactory)
      .withInitialCacheConfigurations(cacheConfigurations)
      .build()
  }
}