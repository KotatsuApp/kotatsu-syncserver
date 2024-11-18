package org.kotatsu.plugins

import io.ktor.server.application.*
import org.kotatsu.database
import org.kotatsu.databaseType
import org.kotatsu.util.DatabaseType
import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect
import org.ktorm.support.postgresql.PostgreSqlDialect
import java.util.*


fun Application.configureDatabase() {
	val config = this.environment.config
	val name = config.property("database.name").getString()
	val host = config.property("database.host").getString()
	val port = config.property("database.port").getString()
	val type = DatabaseType.valueOf(config.property("database.type").getString().uppercase(Locale.getDefault()))
	val driver = when (type) {
		DatabaseType.MYSQL -> "com.mysql.cj.jdbc.Driver"
		DatabaseType.POSTGRESQL -> "org.postgresql.Driver"
	}
	val dialect = when (type) {
		DatabaseType.MYSQL -> MySqlDialect()
		DatabaseType.POSTGRESQL -> PostgreSqlDialect()
	}
	databaseType = type
	database = Database.connect(
		url = "jdbc:${type.name.lowercase(Locale.getDefault())}://$host:$port/$name",
		driver = driver,
		user = config.property("database.user").getString(),
		password = config.property("database.password").getString(),
		dialect = dialect,
	)
}