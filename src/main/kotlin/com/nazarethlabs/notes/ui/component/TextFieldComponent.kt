package com.nazarethlabs.notes.ui.component

import com.intellij.ui.components.JBTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class TextFieldComponent {
    fun build(
        placeholder: String,
        onTextChange: (String) -> Unit,
    ): JBTextField =
        JBTextField().apply {
            emptyText.text = placeholder
            document.addDocumentListener(
                object : DocumentListener {
                    override fun insertUpdate(e: DocumentEvent?) {
                        onTextChange(text ?: "")
                    }

                    override fun removeUpdate(e: DocumentEvent?) {
                        onTextChange(text ?: "")
                    }

                    override fun changedUpdate(e: DocumentEvent?) {
                        onTextChange(text ?: "")
                    }
                },
            )
        }
}
