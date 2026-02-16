package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.awt.Color

@RunWith(MockitoJUnitRunner::class)
class ColorHelperTest {
    private val colorHelper = ColorHelper()

    @Test
    fun `should return default colors when theList is null and not selected`() {
        val note = Note(title = "Test")
        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(null, note, false)
        assertNotNull(backgroundColor)
        assertNotNull(foregroundColor)
    }

    @Test
    fun `should return default colors when theList is null and selected`() {
        val note = Note(title = "Test")
        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(null, note, true)
        assertNotNull(backgroundColor)
        assertNotNull(foregroundColor)
    }

    @Test
    fun `should return note color when note has color and theList is null`() {
        val note = Note(title = "Test", color = NoteColorEnum.BLUE)
        val (backgroundColor, _) = colorHelper.calculateColors(null, note, false)
        assertNotNull(backgroundColor)
    }

    @Test
    fun `should return date label color when theList is null and not selected`() {
        val note = Note(title = "Test")
        val result = colorHelper.calculateDateLabelColor(null, note, false)
        assertNotNull(result)
    }

    @Test
    fun `should return date label color when theList is null and selected`() {
        val note = Note(title = "Test")
        val result = colorHelper.calculateDateLabelColor(null, note, true)
        assertNotNull(result)
    }

    @Test
    fun `should use note color for background when note has custom color`() {
        val note = Note(title = "Test", color = NoteColorEnum.GREEN)
        val (backgroundColor, _) = colorHelper.calculateColors(null, note, false)
        assertEquals(NoteColorEnum.GREEN.color, backgroundColor)
    }

    @Test
    fun `should return dark foreground for light note colors`() {
        val note = Note(title = "Test", color = NoteColorEnum.YELLOW)
        val (_, foregroundColor) = colorHelper.calculateColors(null, note, false)
        assertEquals(Color.BLACK, foregroundColor)
    }
}
