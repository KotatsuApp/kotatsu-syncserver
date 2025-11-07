package org.kotatsu.plugins

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.server.application.*
import io.ktor.server.mustache.Mustache
import org.kotatsu.mustacheFactory

fun Application.configureTemplating() {
    val factory = DefaultMustacheFactory("templates")

    mustacheFactory = factory

    install(Mustache) {
        mustacheFactory = factory
    }
}
