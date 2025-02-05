package com.bentfores.gateway.server.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.gateway")
class RoutesProperties {

  var routes: List<RouteInfo> = listOf()

  data class RouteInfo(
    var id: String = "",
    var uri: String = "",
    var predicates: List<String> = emptyList(),
    var filters: List<String> = emptyList(),
    var metadata: Metadata = Metadata()
  ) {

    data class Metadata(
      var cors: Cors = Cors()
    ) {

      data class Cors(
        var allowedOriginPatterns: List<String> = listOf(),
        var allowedMethods: List<String> = listOf(),
        var allowedHeaders: List<String> = listOf()
      )
    }
  }
}
