package com.nazarethlabs.codex.state

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.ProjectManager
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.SortTypeEnum
import com.nazarethlabs.codex.enum.SortTypeEnum.DATE
import com.nazarethlabs.codex.listener.NoteEventListener
import com.nazarethlabs.codex.listener.NoteListener
import com.nazarethlabs.codex.listener.NotesStateListener
import com.nazarethlabs.codex.service.note.NotesSortService
import com.nazarethlabs.codex.service.note.SearchNoteService
import com.nazarethlabs.codex.service.settings.NotesSettingsService

@Service(Service.Level.APP)
class NotesStateManager : NoteListener {
    private val listeners = mutableListOf<NotesStateListener>()
    private val notesSortService = NotesSortService()
    private val searchNoteService = SearchNoteService()

    private var currentNotes: List<Note> = emptyList()
    private var currentSortType: SortTypeEnum = DATE
    private var isSearchActive = false
    private var currentSearchText = ""

    init {
        currentSortType = NotesSettingsService().getDefaultSortType()
        registerToAllProjects()
        refreshNotes()
    }

    private fun registerToAllProjects() {
        ProjectManager.getInstance().openProjects.forEach { project ->
            NoteEventListener.getInstance(project).addListener(this)
        }
    }

    fun addListener(listener: NotesStateListener) {
        listeners.add(listener)
        listener.onNotesStateChanged(currentNotes)
    }

    fun removeListener(listener: NotesStateListener) {
        listeners.remove(listener)
    }

    fun sortBy(sortType: SortTypeEnum) {
        currentSortType = sortType
        isSearchActive = false
        currentSearchText = ""
        refreshNotes()
    }

    fun search(searchText: String) {
        currentSearchText = searchText
        isSearchActive = searchText.isNotEmpty()

        currentNotes =
            if (isSearchActive) {
                searchNoteService.filterNotes(searchText)
            } else {
                notesSortService.refreshList(currentSortType)
            }

        notifyListeners()
    }

    fun clearSearch() {
        isSearchActive = false
        currentSearchText = ""
        refreshNotes()
    }

    fun getCurrentNotes(): List<Note> = currentNotes

    fun isSearchActive(): Boolean = isSearchActive

    fun refresh() {
        refreshNotes()
    }

    override fun onNoteCreated() {
        refreshNotes()
    }

    override fun onNoteUpdated() {
        refreshNotes()
    }

    override fun onNoteDeleted() {
        refreshNotes()
    }

    private fun refreshNotes() {
        currentNotes =
            if (isSearchActive) {
                searchNoteService.filterNotes(currentSearchText)
            } else {
                notesSortService.refreshList(currentSortType)
            }
        notifyListeners()
    }

    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener.onNotesStateChanged(currentNotes)
        }
    }

    companion object {
        fun getInstance(): NotesStateManager = service()
    }
}
