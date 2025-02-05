pluginManagement {
  val kotlinJvmPluginVersion: String by settings
  val springBootVersion: String by settings
  val dependencyManagementPluginVersion: String by settings
  val jibPluginVersion: String by settings

  plugins {
    kotlin("jvm") version kotlinJvmPluginVersion
    kotlin("plugin.spring") version kotlinJvmPluginVersion
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version dependencyManagementPluginVersion
    id("com.google.cloud.tools.jib") version jibPluginVersion
  }
}

rootProject.name = "bentfores-gateway"

include("server")

findProject(":server")?.name = "${rootProject.name}-server"
