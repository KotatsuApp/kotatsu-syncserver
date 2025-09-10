package org.kotatsu

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.kotatsu.mail.MailSender
import org.kotatsu.plugins.*
import org.ktorm.database.Database

lateinit var hikariDataSource: HikariDataSource
lateinit var database: Database
lateinit var mailService: MailSender

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    runMigrations()
    configureSerialization()
    configureMail()
    configureDatabase()
    configureAuthentication()
    configureLogging()
    configureCompression()
    configureStatusPages()
    configureRouting()
}
