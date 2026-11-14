package com.nazarethlabs.codex.helper

import io.sentry.Sentry
import io.sentry.SentryLevel

object SentryHelper {
    private var initialized = false

    fun init() {
        if (initialized) return

        val dsn = System.getenv("SENTRY_DSN") ?: ""
        if (dsn.isBlank()) return

        Sentry.init { options ->
            options.dsn = dsn
            options.tracesSampleRate = 1.0
            options.isDebug = false
        }
        initialized = true
    }

    fun captureException(exception: Throwable) {
        Sentry.captureException(exception)
    }

    fun captureMessage(
        message: String,
        level: SentryLevel = SentryLevel.ERROR,
    ) {
        Sentry.captureMessage(message, level)
    }

    fun isInitialized(): Boolean = initialized
}
