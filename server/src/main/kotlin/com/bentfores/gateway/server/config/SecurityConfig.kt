//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpMethod
//import org.springframework.http.HttpStatus
//import org.springframework.security.config.Customizer
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
//import org.springframework.security.config.web.server.ServerHttpSecurity
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.context.SecurityContextImpl
//import org.springframework.security.web.AuthenticationEntryPoint
//import org.springframework.security.web.server.SecurityWebFilterChain
//import org.springframework.security.web.server.ServerAuthenticationEntryPoint
//import org.springframework.security.web.server.WebFilterExchange
//import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
//import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
//import org.springframework.web.cors.reactive.CorsConfigurationSource
//import reactor.core.publisher.Mono
//import java.net.URI

//package com.bentfores.gateway.server.config
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.web.server.ServerHttpSecurity
//import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
//import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
//import org.springframework.security.web.server.SecurityWebFilterChain
//
//@Configuration
//@EnableWebSecurity
//class SecurityConfig(
//  @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//  private val issuerUri: String,
//  private val corsConfig: CorsConfig
//) {
//
//  @Bean
//  fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
//    http
//      .cors { corsSpec ->
//        corsSpec.configurationSource(corsConfig.corsConfigurationSource())
//      }
//      .authorizeExchange { exchanges ->
//        exchanges
//          .anyExchange().authenticated()
//      }
//      .oauth2ResourceServer { oauth2 ->
//        oauth2.jwt { jwt ->
//          jwt.jwtDecoder(
//            jwtDecoder()
//          )
//        }
//      }
//
//    return http.build()
//  }
//
//  @Bean
//  fun jwtDecoder(): ReactiveJwtDecoder = NimbusReactiveJwtDecoder
//    .withIssuerLocation(issuerUri)
//    .build()
//}
//
//@Configuration
//@EnableWebFluxSecurity
//class SecurityConfig(
//  private val corsConfigurationSource: CorsConfigurationSource,
//) {
//
//  @Bean
//  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
//    return http
//      .csrf { it.disable() }
//      .cors {
//        it.configurationSource(corsConfigurationSource)
//      }
////      .securityContextRepository(WebSessionServerSecurityContextRepository())
//      .authorizeExchange {
//        it.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//        it.pathMatchers("/management/**", "/analysis/**", "/external/**").authenticated()
//        it.pathMatchers("/oauth2/**", "/login/**").permitAll()
//        it.anyExchange().permitAll()
//      }
//      .exceptionHandling {
//        it.authenticationEntryPoint(authenticationEntryPoint())
//      }
////      .oauth2Login {
////        it.authenticationSuccessHandler(CustomSuccessHandler())
////      }
//      .oauth2Login(Customizer.withDefaults())
//      .oauth2ResourceServer { oauth2 ->
//        oauth2.jwt(Customizer.withDefaults())
//      }
//      .build()
//  }
//
//  @Bean
//  fun authenticationEntryPoint(): ServerAuthenticationEntryPoint {
//    return ServerAuthenticationEntryPoint { exchange, e ->
//      exchange.response.statusCode = HttpStatus.UNAUTHORIZED
//      Mono.empty()
//    }
//  }
//
//  class CustomSuccessHandler : ServerAuthenticationSuccessHandler {
//
//    private val securityContextRepository = WebSessionServerSecurityContextRepository()
//
//    override fun onAuthenticationSuccess(
//      exchange: WebFilterExchange,
//      authentication: Authentication
//    ): Mono<Void> {
//      val securityContext = SecurityContextImpl(authentication)
//      return securityContextRepository.save(exchange.exchange, securityContext)
//        .then<Void>(Mono.fromRunnable {
//          exchange.exchange.response.statusCode = HttpStatus.FOUND
//          exchange.exchange.response.headers.location = URI.create("http://localhost:3000/not_processed")
//          exchange.exchange.response.headers.origin = "http://localhost:3000"
//        })
//        .then(exchange.exchange.response.setComplete())
//    }
//  }
//}