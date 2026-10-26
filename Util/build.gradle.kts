plugins {
    kotlin("jvm") version "2.4.20"
}

group = "io.holzfa.bloom"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}