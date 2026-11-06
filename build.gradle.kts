plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.signal"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "1.3.3"

dependencies {
    // Spring Boot Core Web & REST
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Database & Migrations
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // RSS/Atom & HTML Parsing
    implementation("com.rometools:rome:2.1.0")
    implementation("org.jsoup:jsoup:1.18.3")

    // Resilience & In-Process Caching
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
    implementation("com.github.ben-manes.caffeine:caffeine")

    // API Documentation (OpenAPI 3 / Swagger UI)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // YAML Configuration Parsing
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")

    // Spring Modulith (Modular Monolith Boundaries)
    implementation("org.springframework.modulith:spring-modulith-starter-core")

    // Development Tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.wiremock:wiremock-standalone:3.12.0")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-parameters"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
