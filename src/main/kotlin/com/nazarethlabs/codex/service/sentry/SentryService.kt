package com.nazarethlabs.codex.service.sentry

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.protocol.Message

@Service(Service.Level.APP)
class SentryService {
    fun init() {
        val dsn = System.getenv("SENTRY_DSN") ?: ""
        if (dsn.isNotBlank()) {
            Sentry.init { options ->
                options.dsn = dsn
            }
        }
    }

    fun captureException(throwable: Throwable) {
        Sentry.captureException(throwable)
    }

    fun captureEvent(eventName: String) {
        val event = SentryEvent()
        val message = Message()
        message.message = eventName
        event.message = message
        event.level = SentryLevel.INFO
        Sentry.captureEvent(event)
    }

    companion object {
        fun getInstance(): SentryService = ApplicationManager.getApplication().getService(SentryService::class.java)
    }
}
