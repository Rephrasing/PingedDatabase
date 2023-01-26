plugins {
    id("java")
    id("io.freefair.lombok") version "6.3.0"
}

group = "io.github.rephrasing"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:4.8.2") // -> https://mvnrepository.com/artifact/org.mongodb/mongodb-driver-sync
    implementation("com.google.code.findbugs:jsr305:3.0.2") // -> https://mvnrepository.com/artifact/com.google.code.findbugs/jsr305
}
