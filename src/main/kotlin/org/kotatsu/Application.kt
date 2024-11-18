package org.kotatsu

import io.ktor.server.application.*
import org.kotatsu.plugins.*
import org.kotatsu.util.DatabaseType
import org.ktorm.database.Database

lateinit var database: Database
lateinit var databaseType: DatabaseType

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
	configureLogging()
	configureAuthentication()
	configureDatabase()
	configureCompression()
	configureSerialization()
	configureAutoHead()
	configureRouting()
	configureStatusPages()
}