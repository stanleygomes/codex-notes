package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.SearchHelper
import com.nazarethlabs.codex.repository.NoteRepository

class SearchNoteService {
    fun filterNotes(filterText: String): List<Note> {
        val noteStorage = NoteRepository.getInstance()
        val allNotes = noteStorage.getAllNotes()
        val contentIndexService = NoteContentIndexService.getInstance()
        val contentMap = contentIndexService.getContentForSearch(allNotes)
        return SearchHelper.search(allNotes, filterText, contentMap)
    }
}
