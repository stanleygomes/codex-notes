package com.nazarethlabs.codex.ui.settings

import org.junit.Assert.assertFalse
import org.junit.Test

class NotesConfigFormComponentTest {
    @Test
    fun `should disable notes directory field when building form`() {
        val component = NotesConfigFormComponent()

        component.build()

        val notesDirectoryField = requireNotNull(component.getNotesDirectoryField())
        assertFalse(notesDirectoryField.isEnabled)
    }
}
