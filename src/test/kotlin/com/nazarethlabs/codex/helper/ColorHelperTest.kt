package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.awt.Color
import javax.swing.JList

@RunWith(MockitoJUnitRunner::class)
class ColorHelperTest {
    @Mock
    private lateinit var theList: JList<Note>

    private lateinit var colorHelper: ColorHelper

    @Before
    fun setUp() {
        colorHelper = ColorHelper()
    }

    @Test
    fun `should return selection colors when item is selected`() {
        `when`(theList.selectionBackground).thenReturn(Color.BLUE)
        `when`(theList.selectionForeground).thenReturn(Color.WHITE)

        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(theList, true)

        assertEquals(Color.BLUE, backgroundColor)
        assertEquals(Color.WHITE, foregroundColor)
    }

    @Test
    fun `should return list default colors when item is not selected`() {
        `when`(theList.background).thenReturn(Color.WHITE)
        `when`(theList.foreground).thenReturn(Color.BLACK)

        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(theList, false)

        assertEquals(Color.WHITE, backgroundColor)
        assertEquals(Color.BLACK, foregroundColor)
    }

    @Test
    fun `should return dimmed selection foreground for secondary color when selected`() {
        `when`(theList.selectionForeground).thenReturn(Color.WHITE)

        val result = colorHelper.calculateSecondaryColor(theList, true)

        assertEquals(Color.WHITE.darker(), result)
    }

    @Test
    fun `should return gray secondary color when not selected with light background`() {
        `when`(theList.background).thenReturn(Color.WHITE)

        val result = colorHelper.calculateSecondaryColor(theList, false)

        assertEquals(Color(120, 120, 120), result)
    }

    @Test
    fun `should return lighter gray secondary color when not selected with dark background`() {
        `when`(theList.background).thenReturn(Color.BLACK)

        val result = colorHelper.calculateSecondaryColor(theList, false)

        assertEquals(Color(150, 150, 150), result)
    }

    @Test
    fun `should return default colors when list is null and selected`() {
        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(null, true)

        assertEquals(Color.WHITE, backgroundColor)
        assertEquals(Color.BLACK, foregroundColor)
    }

    @Test
    fun `should return default colors when list is null and not selected`() {
        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(null, false)

        assertEquals(Color.WHITE, backgroundColor)
        assertEquals(Color.BLACK, foregroundColor)
    }

    @Test
    fun `should return default secondary color when list is null and selected`() {
        val result = colorHelper.calculateSecondaryColor(null, true)

        assertEquals(Color.BLACK.darker(), result)
    }

    @Test
    fun `should return default secondary color when list is null and not selected`() {
        val result = colorHelper.calculateSecondaryColor(null, false)

        assertEquals(Color(120, 120, 120), result)
    }
}
