package com.nazarethlabs.codex.service.sentry

import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SentryServiceTest {
    private lateinit var sentryService: SentryService

    @Before
    fun setUp() {
        sentryService = SentryService()
    }

    @After
    fun tearDown() {
        Sentry.close()
    }

    @Test
    fun `should not initialize sentry when dsn is empty`() {
        sentryService.init()

        assertEquals(false, Sentry.isEnabled())
    }

    @Test
    fun `should call captureException without error when sentry is not initialized`() {
        val exception = RuntimeException("test error")

        sentryService.captureException(exception)
    }

    @Test
    fun `should call captureEvent without error when sentry is not initialized`() {
        sentryService.captureEvent("test.event")
    }

    @Test
    fun `should create sentry event with correct message and level`() {
        var capturedEvent: SentryEvent? = null

        Sentry.init { options ->
            options.dsn = "https://key@sentry.io/123"
            options.beforeSend =
                io.sentry.SentryOptions.BeforeSendCallback { event, _ ->
                    capturedEvent = event
                    null
                }
        }

        sentryService.captureEvent("note.created")

        assertNotNull(capturedEvent)
        assertEquals("note.created", capturedEvent!!.message!!.message)
        assertEquals(SentryLevel.INFO, capturedEvent!!.level)
    }

    @Test
    fun `should capture exception with correct throwable`() {
        var capturedException: Throwable? = null

        Sentry.init { options ->
            options.dsn = "https://key@sentry.io/123"
            options.beforeSend =
                io.sentry.SentryOptions.BeforeSendCallback { event, _ ->
                    capturedException = event.throwable
                    null
                }
        }

        val exception = RuntimeException("test error")
        sentryService.captureException(exception)

        assertNotNull(capturedException)
        assertEquals("test error", capturedException!!.message)
    }
}
