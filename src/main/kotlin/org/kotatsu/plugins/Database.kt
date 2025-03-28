package org.kotatsu.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.kotatsu.database
import org.ktorm.database.Database

fun Application.configureDatabase() {
	val name = environment.config.property("database.name").getString()
	val host = environment.config.property("database.host").getString()
	val port = environment.config.property("database.port").getString()

	val config = HikariConfig().apply {
		jdbcUrl = "jdbc:mysql://$host:$port/$name"
		username = environment.config.property("database.user").getString()
		password = environment.config.property("database.password").getString()
		driverClassName = "com.mysql.cj.jdbc.Driver"
	}

	val ds = HikariDataSource(config)
	database = Database.connect(ds)
}