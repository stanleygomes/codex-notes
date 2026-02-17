package com.nazarethlabs.codex.ui.noteslist

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel

@RunWith(MockitoJUnitRunner::class)
class NoteListItemComponentTest {
    @Mock
    private lateinit var theList: JList<Note>

    private lateinit var component: NoteListItemComponent

    @Before
    fun setUp() {
        component = NoteListItemComponent()
        `when`(theList.background).thenReturn(Color.WHITE)
        `when`(theList.foreground).thenReturn(Color.BLACK)
    }

    @Test
    fun `should return empty panel when note is null`() {
        val panel = component.build(theList = theList, note = null, isSelected = false)
        assertEquals(0, panel.componentCount)
    }

    @Test
    fun `should display title with relatively larger font`() {
        val note = Note(id = "1", title = "Test Note", updatedAt = System.currentTimeMillis())

        val panel = component.build(theList = theList, note = note, isSelected = false)

        val titleLabel = findLabelByText(panel, "Test Note")
        assertTrue(titleLabel != null)
        val defaultSize = JLabel().font.size.toFloat()
        assertTrue(titleLabel!!.font.size >= defaultSize)
    }

    @Test
    fun `should display content preview when provided`() {
        val note = Note(id = "1", title = "Test Note", updatedAt = System.currentTimeMillis())

        val panel = component.build(theList = theList, note = note, isSelected = false, contentPreview = "Preview text...")

        val previewLabel = findLabelByText(panel, "Preview text...")
        assertTrue(previewLabel != null)
    }

    @Test
    fun `should not display content preview when empty`() {
        val note = Note(id = "1", title = "Test Note", updatedAt = System.currentTimeMillis())

        val panel = component.build(theList = theList, note = note, isSelected = false, contentPreview = "")

        val previewLabel = findLabelByText(panel, "")
        assertTrue(previewLabel == null)
    }

    @Test
    fun `should show color stripe when note has color`() {
        val note = Note(id = "1", title = "Test Note", updatedAt = System.currentTimeMillis(), color = NoteColorEnum.BLUE)

        val panel = component.build(theList = theList, note = note, isSelected = false)

        val layout = panel.layout as BorderLayout
        val westComponent = layout.getLayoutComponent(BorderLayout.WEST)
        assertTrue(westComponent is JPanel)
        assertEquals(NoteColorEnum.BLUE.color, westComponent.background)
    }

    @Test
    fun `should not show color stripe when note has no color`() {
        val note = Note(id = "1", title = "Test Note", updatedAt = System.currentTimeMillis(), color = NoteColorEnum.NONE)

        val panel = component.build(theList = theList, note = note, isSelected = false)

        val layout = panel.layout as BorderLayout
        val westComponent = layout.getLayoutComponent(BorderLayout.WEST)
        assertTrue(westComponent == null)
    }

    @Test
    fun `should use selection colors when item is selected`() {
        `when`(theList.selectionBackground).thenReturn(Color.BLUE)
        `when`(theList.selectionForeground).thenReturn(Color.WHITE)
        val note = Note(id = "1", title = "Test Note", updatedAt = System.currentTimeMillis())

        val panel = component.build(theList = theList, note = note, isSelected = true)

        assertEquals(Color.BLUE, panel.background)
    }

    @Test
    fun `should not use note color as background when not selected`() {
        val note = Note(id = "1", title = "Test Note", updatedAt = System.currentTimeMillis(), color = NoteColorEnum.GREEN)

        val panel = component.build(theList = theList, note = note, isSelected = false)

        assertFalse(panel.background == NoteColorEnum.GREEN.color)
        assertEquals(Color.WHITE, panel.background)
    }

    private fun findLabelByText(
        container: java.awt.Container,
        text: String,
    ): JLabel? {
        for (comp in container.components) {
            if (comp is JLabel && comp.text == text) return comp
            if (comp is java.awt.Container) {
                val found = findLabelByText(comp, text)
                if (found != null) return found
            }
        }
        return null
    }
}
