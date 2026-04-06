plugins {
    kotlin("jvm") version "2.3.0"
}

group = "dev.jovanni0"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))

    implementation("com.positiondev.epublib:epublib-core:3.1")
    implementation("com.vladsch.flexmark:flexmark-html2md-converter:0.64.8")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}