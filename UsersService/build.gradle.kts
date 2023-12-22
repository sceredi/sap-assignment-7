plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "sap.microservices"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.vertx:vertx-core:4.4.6")
    implementation("io.vertx:vertx-web:4.4.6")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("LauncherKt")
}