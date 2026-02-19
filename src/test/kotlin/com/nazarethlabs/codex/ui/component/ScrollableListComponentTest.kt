package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.JList
import javax.swing.JScrollPane

@RunWith(MockitoJUnitRunner::class)
class ScrollableListComponentTest {
    @Test
    fun `should create scroll pane wrapping list`() {
        val list = JList<String>()
        val component = ScrollableListComponent(list)

        val scrollPane = component.build()

        assertNotNull(scrollPane)
        assertTrue(scrollPane is JScrollPane)
    }

    @Test
    fun `should have null border on scroll pane`() {
        val list = JList<String>()
        val component = ScrollableListComponent(list)

        val scrollPane = component.build()

        assertNull(scrollPane.border)
    }

    @Test
    fun `should contain the provided list in viewport`() {
        val list = JList<String>()
        val component = ScrollableListComponent(list)

        val scrollPane = component.build()

        assertNotNull(scrollPane.viewport.view)
    }

    @Test
    fun `should disable horizontal scroll bar`() {
        val list = JList<String>()
        val component = ScrollableListComponent(list)

        val scrollPane = component.build()

        assertEquals(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, scrollPane.horizontalScrollBarPolicy)
    }
}
