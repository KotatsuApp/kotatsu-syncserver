package org.kotatsu.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.runMigrations() {
	val dbConfig = environment.config.config("database")

	val host = dbConfig.property("host").getString()
	val port = dbConfig.property("port").getString()
	val dbName = dbConfig.property("name").getString()
	val user = dbConfig.property("user").getString()
	val password = dbConfig.property("password").getString()

	val flyway = Flyway.configure()
		.dataSource("jdbc:mysql://$host:$port/$dbName", user, password)
		.load()

	flyway.migrate()
}