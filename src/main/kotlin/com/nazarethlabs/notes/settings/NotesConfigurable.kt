package com.nazarethlabs.notes.settings

import com.intellij.openapi.options.Configurable
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.service.settings.NotesConfigService
import com.nazarethlabs.notes.ui.settings.NotesConfigComponent
import javax.swing.JComponent

class NotesConfigurable : Configurable {
    private var panelComponent: NotesConfigComponent? = null
    private val configService = NotesConfigService()

    override fun getDisplayName(): String = MessageHelper.getMessage("settings.display.name")

    override fun createComponent(): JComponent {
        panelComponent = NotesConfigComponent()
        return panelComponent!!.build()
    }

    override fun isModified(): Boolean = configService.isModified(panelComponent)

    override fun apply() {
        configService.apply(panelComponent)
    }

    override fun reset() {
        configService.reset(panelComponent)
    }

    override fun disposeUIResources() {
        panelComponent = null
    }
}
