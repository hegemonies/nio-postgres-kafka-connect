import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val coroutineVersion = "1.7.1"

plugins {
    val kotlinVersion = "1.8.21"

    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
    id("com.google.cloud.tools.jib") version "3.3.2"
}

group = "hegemonies"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // data
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    // web
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.5.1")

    // kotlin
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutineVersion")

    // kafka
    implementation("org.springframework.kafka:spring-kafka")

    // logger
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    // metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // postgres
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // utils
    developmentOnly("io.netty:netty-resolver-dns-native-macos:4.1.90.Final:osx-aarch_64")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from {
        image = "azul/zulu-openjdk:17-latest"
    }

    to {
        image = "hegemonies/nio-postgres-kafka-connect:$version"
    }
}
