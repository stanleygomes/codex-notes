package com.nazarethlabs.notes.service.note

import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.repository.NoteStorageRepository

class NotesSortService {
    private val noteStorage = NoteStorageRepository()

    fun refreshList(currentSortTypeEnum: SortTypeEnum): List<Note> =
        when (currentSortTypeEnum) {
            SortTypeEnum.TITLE -> noteStorage.getAllNotes().sortedBy { it.title.lowercase() }
            SortTypeEnum.DATE -> noteStorage.getAllNotes().sortedByDescending { it.updatedAt }
            SortTypeEnum.FAVORITE ->
                noteStorage.getAllNotes().sortedWith(
                    compareByDescending<Note> { it.isFavorite }.thenByDescending { it.updatedAt },
                )
        }
}
