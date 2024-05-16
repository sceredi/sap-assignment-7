plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "sap.microservices"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("io.mockk:mockk:1.13.10")
    implementation("io.vertx:vertx-core:4.4.6")
    implementation("io.vertx:vertx-web:4.4.6")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}
