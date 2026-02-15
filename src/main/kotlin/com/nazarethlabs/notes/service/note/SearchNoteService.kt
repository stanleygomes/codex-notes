package com.nazarethlabs.notes.service.note

import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.helper.SearchHelper
import com.nazarethlabs.notes.repository.NoteStorageRepository

class SearchNoteService {
    fun filterNotes(filterText: String): List<Note> {
        val noteStorage = NoteStorageRepository()
        return SearchHelper.search(noteStorage.getAllNotes(), filterText)
    }
}
