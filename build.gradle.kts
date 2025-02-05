import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") apply false
  kotlin("plugin.spring") apply false
  id("org.springframework.boot") apply false
  id("io.spring.dependency-management") apply false
  id("com.google.cloud.tools.jib") apply false
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  apply(plugin = "io.spring.dependency-management")

  repositories {
    mavenCentral()
  }

  val kotlinLoggingVersion: String by project
  val logbookVersion: String by project
  val springCloudVersion: String by project
  val logstashEncoderVersion: String by project

  configure<DependencyManagementExtension> {
    imports {
      mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }

    dependencies {
      dependency("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")
      dependency("org.zalando:logbook-spring-boot-starter:$logbookVersion")
      dependency("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")
    }
  }

  val javaTargetVersion: String by project

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs += "-Xjsr305=strict"
      jvmTarget = javaTargetVersion
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}
