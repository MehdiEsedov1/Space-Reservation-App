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

    // JDBC driver
    implementation("org.postgresql:postgresql:42.7.1")

    // JPA and Hibernate (without Spring)
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
}

tasks.test {
    useJUnitPlatform()
}
