import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val coroutineVersion = "1.8.0"

plugins {
    val kotlinVersion = "1.9.21"

    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("com.google.cloud.tools.jib") version "3.4.1"
}

group = "hegemonies"
version = "1.0.0"
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
        image = "bellsoft/liberica-runtime-container:jre-21-slim-glibc"
    }

    to {
        image = "hegemonies/nio-postgres-kafka-connect:$version"
    }
}
