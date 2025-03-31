package org.kotatsu

import io.ktor.server.application.*
import org.kotatsu.plugins.*
import org.ktorm.database.Database

lateinit var database: Database

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
	configureSerialization()
	configureDatabase()
	configureAuthentication()
	configureLogging()
	configureCompression()
	configureStatusPages()
	configureRouting()
}