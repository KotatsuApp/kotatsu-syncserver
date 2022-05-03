package org.kotatsu

import io.ktor.server.application.*
import org.kotatsu.plugins.*
import org.ktorm.database.Database
import org.ktorm.support.sqlite.SQLiteDialect
import org.sqlite.javax.SQLiteConnectionPoolDataSource

lateinit var database: Database

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
	configureLogging()
	configureCompression()
	configureRouting()
	configureSerialization()
	configureAuthentication()

	val dataSource = SQLiteConnectionPoolDataSource()
	dataSource.url = "jdbc:sqlite:kotatsu.db"
	database = Database.connect(dataSource, SQLiteDialect())
}