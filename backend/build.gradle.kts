import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "tiago.canilhas"
version = "0.1.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")

	// JDBI
	implementation("org.jdbi:jdbi3-core:3.52.0")
	implementation("org.jdbi:jdbi3-kotlin:3.52.0")
	implementation("org.jdbi:jdbi3-postgres:3.52.0")
	implementation("org.postgresql:postgresql")

	// Tests
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("io.mockk:mockk:1.13.10")					// Service Layer mocking
	testImplementation("com.ninja-squad:springmockk:4.0.2")			// Http Layer mocking
}

kotlin {
	jvmToolchain(24)

	compilerOptions {
		freeCompilerArgs.addAll(
			"-Xjsr305=strict",
			"-Xannotation-default-target=param-property",
			"-XXLanguage:+NestedTypeAliases"
		)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}



val dockerComposePath = File(projectDir, "docker-compose.yml").absolutePath

/**
 *		Development
 */

tasks.register<Exec>("dbDevUp") {
	group = "docker-dev"
	description = "Starts the development database container (db)."
	commandLine("docker", "compose", "-f", dockerComposePath, "up", "-d", "--build", "--force-recreate", "db")
}

tasks.register<Exec>("dbDevDown") {
	group = "docker-dev"
	description = "Stops and removes the dev container and all its data."
	commandLine("docker", "compose", "-f", dockerComposePath, "rm", "-f", "-s", "-v", "db")
}



/**
 *		Tests
 */

tasks.register<Exec>("dbTestsUp") {
	group = "docker-tests"
	description = "Starts the database container for testing."
	commandLine("docker", "compose", "-f", dockerComposePath, "up", "-d", "--build", "--force-recreate", "db-tests")
}

tasks.register<Exec>("dbTestsWait") {
	group = "docker-tests"
	description = "Waits until Postgres is accepting connections."
	dependsOn("dbTestsUp")

	commandLine("docker", "exec", "db-tests", "sh", "-c",
		"until pg_isready -U dbuser -d db; do echo 'Waiting for Postgres...'; sleep 1; done"
	)
}

tasks.register<Exec>("dbTestsDown") {
	group = "docker-tests"
	description = "Stops and removes the test database container."
	commandLine("docker", "compose", "-f", dockerComposePath, "rm", "-f", "-s", "-v", "db-tests")
}

tasks.test {
	useJUnitPlatform()

	dependsOn("dbTestsWait")
	environment("DB_URL", "jdbc:postgresql://localhost:5433/db?user=dbuser&password=changeit")
	finalizedBy("dbTestsDown")
}