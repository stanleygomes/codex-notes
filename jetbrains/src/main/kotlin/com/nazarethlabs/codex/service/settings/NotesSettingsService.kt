package com.nazarethlabs.codex.service.settings

import com.nazarethlabs.codex.enum.SortTypeEnum
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.repository.NotesSettingsRepository

class NotesSettingsService {
    private fun normalizeNotesDirectory(directory: String): String = directory.ifBlank { FileHelper.getDefaultNotesDir() }

    fun getDefaultFileExtension(): String =
        NotesSettingsRepository
            .getInstance()
            .getDefaultFileExtension()

    fun setDefaultFileExtension(extension: String) {
        NotesSettingsRepository
            .getInstance()
            .setDefaultFileExtension(extension)
    }

    fun getDefaultSortType(): SortTypeEnum =
        NotesSettingsRepository
            .getInstance()
            .getDefaultSortType()

    fun setDefaultSortType(sortType: SortTypeEnum) {
        NotesSettingsRepository
            .getInstance()
            .setDefaultSortType(sortType)
    }

    fun getNotesDirectory(): String =
        NotesSettingsRepository
            .getInstance()
            .getNotesDirectory()
            .let(::normalizeNotesDirectory)

    fun setNotesDirectory(directory: String) {
        NotesSettingsRepository
            .getInstance()
            .setNotesDirectory(normalizeNotesDirectory(directory))
    }
}
