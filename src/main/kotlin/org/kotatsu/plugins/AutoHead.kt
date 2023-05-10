package org.kotatsu.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*

fun Application.configureAutoHead() {
	install(AutoHeadResponse)
}