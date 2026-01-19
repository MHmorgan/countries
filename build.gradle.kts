plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("MainKt")
    applicationName = "countries"
}

repositories {
    mavenCentral()
}

dependencies {
    // CLI
    implementation("com.github.ajalt.clikt:clikt:5.0.3")

    // Terminal formatting
    implementation("com.github.ajalt.mordant:mordant:3.0.2")

    // HTTP Client
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-cio:3.1.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.0")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.4")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.15")

    // Testing
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.1")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("io.ktor:ktor-client-mock:3.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
