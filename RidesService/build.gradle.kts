plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "it.unibo.sap"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("io.mockk:mockk:1.13.10")
    implementation("io.vertx:vertx-core:4.4.6")
    implementation("io.vertx:vertx-web:4.4.6")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")
    implementation("org.danilopianini:khttp:1.4.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("it.unibo.sap.MainKt")
}
