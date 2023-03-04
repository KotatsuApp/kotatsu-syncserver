package org.kotatsu.plugins

import io.ktor.server.application.*
import org.kotatsu.database
import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect

fun Application.configureDatabase() {
	val config = this.environment.config
	val name = config.property("database.name").getString()
	val host = config.property("database.host").getString()
	val port = config.property("database.port").getString()
	database = Database.connect(
		url = "jdbc:mysql://$host:$port/$name",
		driver = "com.mysql.cj.jdbc.Driver",
		user = config.property("database.user").getString(),
		password = config.property("database.password").getString(),
		dialect = MySqlDialect(),
	)
}