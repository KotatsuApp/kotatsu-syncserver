package org.kotatsu

import com.github.mustachejava.MustacheFactory
import io.ktor.server.application.*
import com.zaxxer.hikari.HikariDataSource
import org.kotatsu.mail.MailSender
import org.kotatsu.plugins.*
import org.ktorm.database.Database

lateinit var hikariDataSource: HikariDataSource
lateinit var database: Database
lateinit var mailService: MailSender
lateinit var mustacheFactory: MustacheFactory

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
    configureRouting()
}
