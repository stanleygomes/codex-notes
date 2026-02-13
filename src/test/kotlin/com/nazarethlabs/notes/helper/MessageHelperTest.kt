package com.nazarethlabs.notes.helper

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MessageHelperTest {

    @Test
    fun `should return message when key exists`() {
        val key = "time.now"
        val result = MessageHelper.getMessage(key)
        assertEquals("now", result)
    }

    @Test
    fun `should return message with parameters when key exists`() {
        val key = "dialog.delete.note.message"
        val param = "Test Note"
        val result = MessageHelper.getMessage(key, param)
        assertEquals("Are you sure you want to delete the note \"Test Note\"?", result)
    }
}
