package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.SearchHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository

class SearchNoteService {
    fun filterNotes(filterText: String): List<Note> {
        val noteStorage = NoteStorageRepository()
        return SearchHelper.search(noteStorage.getAllNotes(), filterText)
    }
}
