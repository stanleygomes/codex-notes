package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.awt.Color
import javax.swing.JToggleButton

@RunWith(MockitoJUnitRunner::class)
class ChipButtonComponentTest {
    @Test
    fun `should create text chip button`() {
        val component = ChipButtonComponent()

        val chip = component.build("Filter", onToggle = {})

        assertNotNull(chip)
        assertTrue(chip is JToggleButton)
    }

    @Test
    fun `should create chip button with text`() {
        val component = ChipButtonComponent()

        val chip = component.build("Today", onToggle = {})

        assertTrue(chip.text == "Today")
    }

    @Test
    fun `should start in unselected state`() {
        val component = ChipButtonComponent()

        val chip = component.build("Filter", onToggle = {})

        assertFalse(chip.isSelected)
    }

    @Test
    fun `should call onToggle when toggled`() {
        var toggled = false
        val component = ChipButtonComponent()

        val chip = component.build("Filter", onToggle = { toggled = it })
        chip.doClick()

        assertTrue(toggled)
    }

    @Test
    fun `should not be content area filled`() {
        val component = ChipButtonComponent()

        val chip = component.build("Filter", onToggle = {})

        assertFalse(chip.isContentAreaFilled)
    }

    @Test
    fun `should create color chip button`() {
        val component = ChipButtonComponent()

        val chip = component.buildColorChip(Color.BLUE, "Blue", onToggle = {})

        assertNotNull(chip)
        assertTrue(chip.toolTipText == "Blue")
    }

    @Test
    fun `should create color chip with tooltip`() {
        val component = ChipButtonComponent()

        val chip = component.buildColorChip(Color.RED, "Red filter", onToggle = {})

        assertTrue(chip.toolTipText == "Red filter")
    }
}
