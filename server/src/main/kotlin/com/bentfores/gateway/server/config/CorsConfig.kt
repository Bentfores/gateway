package com.bentfores.gateway.server.config

import com.bentfores.gateway.server.config.properties.RoutesProperties
import org.springframework.cloud.gateway.route.RouteDefinition
import org.springframework.cloud.gateway.route.RouteDefinitionLocator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.kotlin.core.publisher.toFlux

@Configuration
class CorsConfig(
  private val routeDefinitionLocator: RouteDefinitionLocator,
  private val routesProperties: RoutesProperties
) {

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    return routesProperties.routes.toFlux()
      .map { into ->
        CorsConfiguration().apply {
          val cors = into.metadata.cors

          allowedOriginPatterns = cors.allowedOriginPatterns
          allowedMethods = cors.allowedMethods
          allowedHeaders = cors.allowedHeaders
          allowCredentials = true
        }
      }
      .zipWith(routeDefinitionLocator.routeDefinitions)
      .collectList()
      .map { configurations ->
        UrlBasedCorsConfigurationSource().apply {
          val conf = configurations.first()
          this.registerCorsConfiguration(
            "/**",
            conf.t1
          )
        }
      }
      .block()!!
  }

  private fun getPathFromRoute(definition: RouteDefinition): String {
    return definition.predicates.find { it.name == "Path" }
      ?.args?.get("_genkey_0") ?: "/**"
  }
}
