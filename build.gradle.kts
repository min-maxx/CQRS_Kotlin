import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    application
}

group = "cqrs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.0.0.M3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}