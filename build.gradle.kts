plugins {
    `maven-publish`
    `java-library`
    id("java")
    id("io.freefair.lombok") version "6.3.0"
}

group = "io.github.rephrasing.pinged"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:4.8.2") // -> https://mvnrepository.com/artifact/org.mongodb/mongodb-driver-sync
    compileOnly("com.google.code.findbugs:jsr305:3.0.2") // -> https://mvnrepository.com/artifact/com.google.code.findbugs/jsr30
    api("com.google.code.gson:gson:2.10.1") // https://mvnrepository.com/artifact/com.google.code.gson/gson
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
