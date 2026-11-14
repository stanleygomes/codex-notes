package com.nazarethlabs.codex.helper

import io.sentry.Sentry
import io.sentry.SentryLevel
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mockStatic
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SentryHelperTest {
    @Test
    fun `should capture exception when called`() {
        mockStatic(Sentry::class.java).use { mockedSentry ->
            val exception = RuntimeException("test error")

            SentryHelper.captureException(exception)

            mockedSentry.verify { Sentry.captureException(exception) }
        }
    }

    @Test
    fun `should capture message with default error level`() {
        mockStatic(Sentry::class.java).use { mockedSentry ->
            SentryHelper.captureMessage("test message")

            mockedSentry.verify { Sentry.captureMessage("test message", SentryLevel.ERROR) }
        }
    }

    @Test
    fun `should capture message with custom level`() {
        mockStatic(Sentry::class.java).use { mockedSentry ->
            SentryHelper.captureMessage("test warning", SentryLevel.WARNING)

            mockedSentry.verify { Sentry.captureMessage("test warning", SentryLevel.WARNING) }
        }
    }
}
