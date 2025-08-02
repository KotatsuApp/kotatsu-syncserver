import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.ktor)
	alias(libs.plugins.kotlin.plugin.serialization)
	alias(libs.plugins.shadow)
	alias(libs.plugins.flyway)
}

group = "org.kotatsu"
version = "0.0.1"

application {
	mainClass = "io.ktor.server.netty.EngineMain"

	val isDevelopment: Boolean = project.ext.has("development")
	applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

tasks.named<ShadowJar>("shadowJar") {
	mergeServiceFiles()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
	implementation(libs.ktor.server.call.logging)
	implementation(libs.ktor.server.compression)
	implementation(libs.ktor.server.status.pages)
	implementation(libs.ktor.server.auto.head.response)
    implementation(libs.ktorm.core)
    implementation(libs.ktorm.support.mysql)
	implementation(libs.flyway.core)
	implementation(libs.flyway.mysql)
	implementation(libs.hikaricp)
	implementation(libs.mysql.connector.j)
	implementation(libs.mariadb.java.client)
    implementation(libs.logback.classic)
	testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
