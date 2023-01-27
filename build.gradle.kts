plugins {
    `maven-publish`
    id("java")
    id("io.freefair.lombok") version "6.3.0"
}

group = "io.github.rephrasing.sparkbase"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:4.8.2") // -> https://mvnrepository.com/artifact/org.mongodb/mongodb-driver-sync
    compileOnly("com.google.code.findbugs:jsr305:3.0.2") // -> https://mvnrepository.com/artifact/com.google.code.findbugs/jsr305
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
