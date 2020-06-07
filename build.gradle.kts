plugins {
    application
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "xyz.yuurai.seekr"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("de.jensd:fontawesomefx-commons:11.0")
    implementation("de.jensd:fontawesomefx-fontawesome:4.7.0-5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.awaitility:awaitility:4.0.3")
    testImplementation("org.testfx:testfx-core:4.0.16-alpha")
    testImplementation("org.testfx:testfx-junit5:4.0.16-alpha")
//    testImplementation("io.mockk:mockk:1.10.0")
}

application {
    mainClassName = "xyz.yuurai.seekr.app.SeekrApp"
}

javafx {
    modules("javafx.controls", "javafx.fxml", "javafx.swing")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}