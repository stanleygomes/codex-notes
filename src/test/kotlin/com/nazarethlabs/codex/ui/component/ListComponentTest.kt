package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.ListCellRenderer

@RunWith(MockitoJUnitRunner::class)
class ListComponentTest {
    @Test
    fun `should create list with model`() {
        val model = DefaultListModel<String>()
        model.addElement("Item 1")
        model.addElement("Item 2")
        val renderer = JList<String>().cellRenderer

        val component = ListComponent(model, renderer)
        val list = component.build()

        assertEquals(2, list.model.size)
        assertEquals("Item 1", list.model.getElementAt(0))
    }

    @Test
    fun `should create list with cell renderer`() {
        val model = DefaultListModel<String>()
        val renderer = JList<String>().cellRenderer

        val component = ListComponent(model, renderer)
        val list = component.build()

        assertNotNull(list.cellRenderer)
    }

    @Test
    fun `should create empty list when model is empty`() {
        val model = DefaultListModel<String>()
        val renderer = JList<String>().cellRenderer

        val component = ListComponent(model, renderer)
        val list = component.build()

        assertEquals(0, list.model.size)
    }
}
