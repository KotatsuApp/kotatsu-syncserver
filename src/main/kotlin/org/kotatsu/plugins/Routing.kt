package org.kotatsu.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.routing.routing
import org.kotatsu.routes.*

fun Application.configureRouting() {
    install(AutoHeadResponse)
    routing {
        healthRoutes()
        deeplinkRoutes()

        rateLimit(RateLimitName("globalApi")) {
            authRoutes()
            userRoutes()
            favouriteRoutes()
            historyRoutes()
            mangaRoutes()
        }
    }
}
