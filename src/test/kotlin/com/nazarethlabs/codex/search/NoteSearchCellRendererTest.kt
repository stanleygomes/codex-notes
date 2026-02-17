package com.nazarethlabs.codex.search

import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.JList
import javax.swing.JPanel

@RunWith(MockitoJUnitRunner::class)
class NoteSearchCellRendererTest {
    private lateinit var renderer: NoteSearchCellRenderer
    private lateinit var list: JList<Note>

    @Before
    fun setUp() {
        renderer = NoteSearchCellRenderer()
        list = JList<Note>()
    }

    @Test
    fun `should return panel when rendering component`() {
        val note =
            Note(
                id = "1",
                title = "Test Note",
                updatedAt = System.currentTimeMillis(),
            )

        val component =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
    }

    @Test
    fun `should return empty panel when value is null`() {
        val component =
            renderer.getListCellRendererComponent(
                list,
                null,
                0,
                false,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
        assertEquals(0, (component as JPanel).componentCount)
    }

    @Test
    fun `should render note with title`() {
        val note =
            Note(
                id = "1",
                title = "My Important Note",
                updatedAt = System.currentTimeMillis(),
            )

        val component =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
        assertTrue((component as JPanel).componentCount > 0)
    }

    @Test
    fun `should render favorite note with star icon`() {
        val note =
            Note(
                id = "1",
                title = "Favorite Note",
                isFavorite = true,
                updatedAt = System.currentTimeMillis(),
            )

        val component =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
    }

    @Test
    fun `should render non-favorite note without star icon`() {
        val note =
            Note(
                id = "1",
                title = "Regular Note",
                isFavorite = false,
                updatedAt = System.currentTimeMillis(),
            )

        val component =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
    }

    @Test
    fun `should render selected item`() {
        val note =
            Note(
                id = "1",
                title = "Selected Note",
                updatedAt = System.currentTimeMillis(),
            )

        val component =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                true,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
    }

    @Test
    fun `should render unselected item`() {
        val note =
            Note(
                id = "1",
                title = "Unselected Note",
                updatedAt = System.currentTimeMillis(),
            )

        val component =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
    }

    @Test
    fun `should render note with updated date`() {
        val currentTime = System.currentTimeMillis()
        val note =
            Note(
                id = "1",
                title = "Note with Date",
                updatedAt = currentTime,
            )

        val component =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                false,
            )

        assertNotNull(component)
        assertTrue(component is JPanel)
    }

    @Test
    fun `should handle note at different indexes`() {
        val note =
            Note(
                id = "1",
                title = "Test Note",
                updatedAt = System.currentTimeMillis(),
            )

        val component1 = renderer.getListCellRendererComponent(list, note, 0, false, false)
        val component2 = renderer.getListCellRendererComponent(list, note, 5, false, false)
        val component3 = renderer.getListCellRendererComponent(list, note, 10, false, false)

        assertNotNull(component1)
        assertNotNull(component2)
        assertNotNull(component3)
    }

    @Test
    fun `should handle note with and without focus`() {
        val note =
            Note(
                id = "1",
                title = "Test Note",
                updatedAt = System.currentTimeMillis(),
            )

        val componentWithFocus =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                true,
            )
        val componentWithoutFocus =
            renderer.getListCellRendererComponent(
                list,
                note,
                0,
                false,
                false,
            )

        assertNotNull(componentWithFocus)
        assertNotNull(componentWithoutFocus)
    }
}
