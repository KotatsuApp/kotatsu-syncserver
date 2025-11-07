package org.kotatsu.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.minutes

fun Application.configureRateLimit() {
    install(RateLimit) {
        register(RateLimitName("globalApi")) {
            rateLimiter(limit = 100, refillPeriod = 1.minutes)
        }

        register(RateLimitName("auth")) {
            rateLimiter(limit = 5, refillPeriod = 1.minutes)
        }

        register(RateLimitName("forgotPassword")) {
            rateLimiter(limit = 5, refillPeriod = 10.minutes)
        }

        register(RateLimitName("resetPassword")) {
            rateLimiter(limit = 5, refillPeriod = 10.minutes)
        }
    }
}
