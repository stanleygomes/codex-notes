package com.nazarethlabs.notes.service

import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.repository.NotesSettingsRepository

class NotesSettingsService {

    fun getDefaultFileExtension(): String {
        return NotesSettingsRepository
            .getInstance()
            .getDefaultFileExtension()
    }

    fun setDefaultFileExtension(extension: String) {
        NotesSettingsRepository
            .getInstance()
            .setDefaultFileExtension(extension)
    }

    fun getDefaultSortType(): SortTypeEnum {
        return NotesSettingsRepository
            .getInstance()
            .getDefaultSortType()
    }

    fun setDefaultSortType(sortType: SortTypeEnum) {
        NotesSettingsRepository
            .getInstance()
            .setDefaultSortType(sortType)
    }
}
