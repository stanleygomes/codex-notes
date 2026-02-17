package com.nazarethlabs.codex.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import javax.swing.JPanel
import javax.swing.JTextField

class ToolWindowPanelTest {
    @Test
    fun `should focus search text field when found in search panel`() {
        val searchPanel = JPanel()
        val focusableField = FocusableTextField()
        searchPanel.add(JPanel())
        searchPanel.add(focusableField)

        focusSearchField(searchPanel)

        assertTrue(focusableField.focusRequested)
    }

    @Test
    fun `should not focus non text component when search panel has no text field`() {
        val searchPanel = JPanel()
        val focusableComponent = FocusableComponent()
        searchPanel.add(focusableComponent)

        focusSearchField(searchPanel)

        assertFalse(focusableComponent.focusRequested)
    }

    private class FocusableTextField : JTextField() {
        var focusRequested = false

        override fun requestFocusInWindow(): Boolean {
            focusRequested = true
            return true
        }
    }

    private class FocusableComponent : java.awt.Component() {
        var focusRequested = false

        override fun requestFocusInWindow(): Boolean {
            focusRequested = true
            return true
        }
    }
}
