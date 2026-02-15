package com.nazarethlabs.codex.service.settings

import com.nazarethlabs.codex.enum.SortTypeEnum
import com.nazarethlabs.codex.repository.NotesSettingsRepository

class NotesSettingsService {
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
}
