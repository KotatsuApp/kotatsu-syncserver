package org.kotatsu.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import org.slf4j.event.Level

fun Application.configureLogging() {
    install(CallLogging) {
        val loggingLevel = this@configureLogging.environment.config.property("kotatsu.logging_level").getString()
        level = when (loggingLevel) {
            "ERROR" -> Level.ERROR
            "WARN" -> Level.WARN
            "INFO" -> Level.INFO
            "DEBUG" -> Level.DEBUG
            "TRACE" -> Level.TRACE
            else -> Level.INFO
        }
    }
}
