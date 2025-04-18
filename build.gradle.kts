plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.seoulsquad"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}