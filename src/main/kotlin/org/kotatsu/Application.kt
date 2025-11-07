package org.kotatsu

import com.github.mustachejava.MustacheFactory
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.kotatsu.mail.MailSender
import org.kotatsu.plugins.*
import org.ktorm.database.Database

lateinit var database: Database
lateinit var hikariDataSource: HikariDataSource
lateinit var mustacheFactory: MustacheFactory
lateinit var mailService: MailSender

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    runMigrations()

    configureSerialization()
    configureMail()
    configureDatabase()
    configureAuthentication()
    configureLogging()
    configureTemplating()
    configureCompression()
    configureStatusPages()
    configureRateLimit()
    configureRouting()
}
