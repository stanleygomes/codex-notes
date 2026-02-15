package com.nazarethlabs.notes.service.settings

import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.repository.NotesSettingsRepository

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
