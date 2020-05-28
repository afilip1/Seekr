plugins {
    kotlin("jvm") version "1.3.72"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "xyz.yuurai.seekr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
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
}