package com.nazarethlabs.codex.service.settings

import com.nazarethlabs.codex.helper.FileExtensionHelper
import com.nazarethlabs.codex.ui.settings.NotesConfigComponent

class NotesConfigService {
    fun isModified(panelComponent: NotesConfigComponent?): Boolean {
        val settings = NotesSettingsService()
        val currentExtension = FileExtensionHelper.normalizeExtension(panelComponent?.getFileExtensionField()?.text ?: "")
        return currentExtension != settings.getDefaultFileExtension()
    }

    fun apply(panelComponent: NotesConfigComponent?) {
        val settings = NotesSettingsService()
        settings.setDefaultFileExtension(FileExtensionHelper.normalizeExtension(panelComponent?.getFileExtensionField()?.text ?: ""))
    }

    fun reset(panelComponent: NotesConfigComponent?) {
        val settings = NotesSettingsService()
        panelComponent?.getFileExtensionField()?.text = settings.getDefaultFileExtension()
    }
}
