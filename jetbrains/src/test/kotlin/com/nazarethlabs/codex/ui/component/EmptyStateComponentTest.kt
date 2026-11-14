package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel

@RunWith(MockitoJUnitRunner::class)
class EmptyStateComponentTest {
    @Test
    fun `should create panel with label`() {
        val component = EmptyStateComponent()

        val panel = component.build()

        assertNotNull(panel)
        assertTrue(panel is JPanel)
        assertTrue(panel.layout is BorderLayout)
    }

    @Test
    fun `should have centered label in panel`() {
        val component = EmptyStateComponent()

        val panel = component.build()
        val label = panel.components.firstOrNull { it is JLabel } as? JLabel

        assertNotNull(label)
    }

    @Test
    fun `should have bold font on label`() {
        val component = EmptyStateComponent()

        val panel = component.build()
        val label = panel.components.firstOrNull { it is JLabel } as? JLabel

        assertNotNull(label)
        assertTrue(label!!.font.isBold)
    }
}
