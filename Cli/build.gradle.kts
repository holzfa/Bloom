plugins {
    kotlin("jvm") version "2.4.20"
}

group = "io.holzfa.bloom"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Lexer"))
    implementation(project(":Util"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}