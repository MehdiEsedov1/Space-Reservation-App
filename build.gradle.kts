plugins {
    id("java")
}

group = "org.example"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // JSON support
    implementation("com.google.code.gson:gson:2.10.1")

    // SQL
    implementation("org.postgresql:postgresql:42.7.1")
}

tasks.test {
    useJUnitPlatform()
}
