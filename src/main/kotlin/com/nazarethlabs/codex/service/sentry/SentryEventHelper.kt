package com.nazarethlabs.codex.service.sentry

object SentryEventHelper {
    fun captureEvent(eventName: String) {
        SentryService.getInstance().captureEvent(eventName)
    }

    fun captureException(throwable: Throwable) {
        SentryService.getInstance().captureException(throwable)
    }
}
