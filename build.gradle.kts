plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.hateoas:spring-hateoas:2.4.0")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.4.0")
	implementation("org.springdoc:springdoc-openapi-ui:1.8.0")
	implementation("org.springframework.boot:spring-boot-starter-security:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.postgresql:postgresql:42.7.2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	annotationProcessor("org.projectlombok:lombok:1.18.34")
	compileOnly("org.projectlombok:lombok:1.18.34")
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
	testImplementation("org.springframework.boot:spring-boot-testcontainers:3.4.0")
	testImplementation("org.testcontainers:junit-jupiter:1.20.4")
	testImplementation("org.testcontainers:postgresql:1.20.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}