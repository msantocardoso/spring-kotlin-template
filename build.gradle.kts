
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
    kotlin("plugin.jpa") version "1.3.61"
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.0.1"
    id("org.hidetake.swagger.generator") version "2.18.1"
}

group = "br.com.cespec"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val developmentOnly by configurations.creating

dependencyManagement {
    val springCloudVersion = "Hoxton.RELEASE"
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }

    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
}

repositories {
    jcenter()
}

val swaggerVersion = "2.9.2"
val detektVersion = "1.0.1"

dependencies {
    // Basic dependencies:
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("javax.activation:activation:1.1.1")

    // Web dependencies:
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-undertow")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // API Docs
    implementation("io.springfox:springfox-swagger2:$swaggerVersion")
    implementation("io.springfox:springfox-swagger-ui:$swaggerVersion")
    swaggerUI("org.webjars:swagger-ui:3.23.0")

    // PostgreSQL dependencies:
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")

    // Security Dependencies
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // Log dependencies
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    // Test and lint dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    detekt("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    detekt("io.gitlab.arturbosch.detekt:detekt-cli:$detektVersion")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

detekt {
    toolVersion = detektVersion
    input = files("./")
    config = files("./detekt-config.yml")
    autoCorrect = true
}

swaggerSources {
    create("spring-kotlin-template") {
        setInputFile(file("src/main/resources/openapi/spring-kotlin-template.yml"))
        ui.outputDir = file("src/main/resources/static/docs")
        ui.wipeOutputDir = true
    }
}

tasks.processResources {
    dependsOn(tasks["generateSwaggerUI"])
}
