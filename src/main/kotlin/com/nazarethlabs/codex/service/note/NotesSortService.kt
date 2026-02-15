package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.SortTypeEnum
import com.nazarethlabs.codex.repository.NoteStorageRepository

class NotesSortService {
    private val noteStorage = NoteStorageRepository.getInstance()

    fun refreshList(currentSortTypeEnum: SortTypeEnum): List<Note> {
        val allNotes = noteStorage.getAllNotes()
        return when (currentSortTypeEnum) {
            SortTypeEnum.TITLE -> allNotes.sortedBy { it.title.lowercase() }
            SortTypeEnum.DATE -> allNotes.sortedByDescending { it.updatedAt }
            SortTypeEnum.FAVORITE ->
                allNotes.sortedWith(
                    compareByDescending<Note> { it.isFavorite }.thenByDescending { it.updatedAt },
                )
        }
    }
}
