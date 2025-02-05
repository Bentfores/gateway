import org.gradle.api.file.DuplicatesStrategy.EXCLUDE

plugins {
  kotlin("plugin.spring")
  id("org.springframework.boot")
  id("com.google.cloud.tools.jib")
}

dependencies {
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
  implementation("org.springframework.cloud:spring-cloud-starter-gateway")
  implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation ("org.springframework.session:spring-session-data-redis")
  implementation ("org.springframework.boot:spring-boot-starter-data-redis")
  implementation ("jakarta.servlet:jakarta.servlet-api")
  implementation("io.github.oshai:kotlin-logging-jvm")
  implementation("org.zalando:logbook-spring-boot-starter")
  implementation("net.logstash.logback:logstash-logback-encoder")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot {
  buildInfo()
}

tasks.bootJar {
  enabled = true
  duplicatesStrategy = EXCLUDE
}

tasks.jar {
  enabled = false
}

jib {
  setAllowInsecureRegistries(true)

  container {
    mainClass = "com.bentfores.gateway.server.GatewayApplicationKt"
    user = "root"
    containerizingMode = "packaged"
  }

  from {
    image = "openjdk:21-jdk"
    platforms {
      platform {
        architecture = "amd64"
        os = "linux"
      }
    }
  }

  to {
    image = "bentfores-gateway"
    tags = mutableSetOf("${project.version}")
  }
}
