package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TextFieldComponentTest {
    @Test
    fun `should create text field with document listener`() {
        val component = TextFieldComponent()

        val textField = component.build("Search...", onTextChange = {})

        assertNotNull(textField)
        assertTrue(textField.document.defaultRootElement != null)
    }

    @Test
    fun `should trigger callback when text changes`() {
        var capturedText = ""
        val component = TextFieldComponent()

        val textField = component.build("Search...", onTextChange = { capturedText = it })
        textField.text = "hello"

        assertTrue(capturedText == "hello")
    }

    @Test
    fun `should trigger callback when text is removed`() {
        var capturedText = "initial"
        val component = TextFieldComponent()

        val textField = component.build("Search...", onTextChange = { capturedText = it })
        textField.text = "hello"
        textField.text = ""

        assertTrue(capturedText == "")
    }

    @Test
    fun `should set placeholder text`() {
        val component = TextFieldComponent()

        val textField = component.build("Type here...", onTextChange = {})

        assertNotNull(textField.emptyText)
    }
}
