package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.SortTypeEnum
import com.nazarethlabs.codex.repository.NoteStorageRepository

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
