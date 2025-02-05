package com.bentfores.gateway.server.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
  @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private val issuerUri: String,
  private val corsConfig: CorsConfig
) {

  @Bean
  fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    http
      .cors { corsSpec ->
        corsSpec.configurationSource(corsConfig.corsConfigurationSource())
      }
      .authorizeExchange { exchanges ->
        exchanges
          .anyExchange().authenticated()
      }
      .oauth2ResourceServer { oauth2 ->
        oauth2.jwt { jwt ->
          jwt.jwtDecoder(
            jwtDecoder()
          )
        }
      }

    return http.build()
  }

  @Bean
  fun jwtDecoder(): ReactiveJwtDecoder = NimbusReactiveJwtDecoder
    .withIssuerLocation(issuerUri)
    .build()
}
