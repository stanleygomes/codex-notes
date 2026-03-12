package com.nazarethlabs.codex.ui.component

import com.intellij.icons.AllIcons
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.JButton

@RunWith(MockitoJUnitRunner::class)
class ButtonComponentTest {
    @Test
    fun `should create button with icon`() {
        val component = ButtonComponent()

        val button = component.build(AllIcons.General.Add)

        assertNotNull(button)
        assertNotNull(button.icon)
        assertTrue(button is JButton)
    }

    @Test
    fun `should create button with tooltip when provided`() {
        val component = ButtonComponent()

        val button = component.build(AllIcons.General.Add, "Add Note")

        assertNotNull(button.toolTipText)
        assertTrue(button.toolTipText == "Add Note")
    }

    @Test
    fun `should create button without tooltip when not provided`() {
        val component = ButtonComponent()

        val button = component.build(AllIcons.General.Add)

        assertTrue(button.toolTipText == null)
    }

    @Test
    fun `should have border set on button`() {
        val component = ButtonComponent()

        val button = component.build(AllIcons.General.Add)

        assertNotNull(button.border)
    }

    @Test
    fun `should have mouse listener attached for hover effects`() {
        val component = ButtonComponent()

        val button = component.build(AllIcons.General.Add)

        assertTrue(button.mouseListeners.isNotEmpty())
    }
}
