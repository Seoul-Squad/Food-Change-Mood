plugins {
    kotlin("jvm") version "2.0.20"
    jacoco
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
    // tests
    testImplementation("com.google.truth:truth:1.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.12.1")
    testImplementation("io.mockk:mockk:1.13.16")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

jacoco {
    toolVersion = "0.8.11" // latest stable
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // run tests first
    reports {
        xml.required.set(true)     // for CI integration (optional)
        html.required.set(true)    // for local nice browsing
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal() // fail if coverage < 80%
            }
        }
    }
}
