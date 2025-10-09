package org.kotatsu.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.kotatsu.database
import org.kotatsu.hikariDataSource
import org.ktorm.database.Database

fun Application.configureDatabase() {
    val name = environment.config.property("database.name").getString()
    val host = environment.config.property("database.host").getString()
    val port = environment.config.property("database.port").getString()
    val dialect = environment.config.property("database.dialect").getString()

    val jdbcDriver = when (dialect.lowercase()) {
        "mariadb" -> "org.mariadb.jdbc.Driver"
        "mysql" -> "com.mysql.cj.jdbc.Driver"
        else -> error("Unsupported DATABASE_DIALECT: $dialect")
    }

    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:$dialect://$host:$port/$name"
        username = environment.config.property("database.user").getString()
        password = environment.config.property("database.password").getString()
        driverClassName = jdbcDriver
    }

    hikariDataSource = HikariDataSource(config)
    database = Database.connect(hikariDataSource)
}
