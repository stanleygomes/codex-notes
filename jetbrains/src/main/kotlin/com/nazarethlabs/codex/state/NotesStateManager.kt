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
import com.nazarethlabs.codex.service.note.NoteFilterService
import com.nazarethlabs.codex.service.note.NotesSortService
import com.nazarethlabs.codex.service.note.SearchNoteService
import com.nazarethlabs.codex.service.settings.NotesSettingsService

@Service(Service.Level.APP)
class NotesStateManager : NoteListener {
    private val listeners = mutableListOf<NotesStateListener>()
    private val notesSortService = NotesSortService()
    private val searchNoteService = SearchNoteService()
    private val noteFilterService = NoteFilterService()

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
                applySearchFilters(applyFavoritesFilter(searchNoteService.filterNotes(searchText)))
            } else {
                applySearchFilters(applyFavoritesFilter(notesSortService.refreshList(currentSortType)))
            }

        notifyListeners()
    }

    fun clearSearch() {
        isSearchActive = false
        currentSearchText = ""
        refreshNotes()
    }

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
                applySearchFilters(applyFavoritesFilter(searchNoteService.filterNotes(currentSearchText)))
            } else {
                applySearchFilters(applyFavoritesFilter(notesSortService.refreshList(currentSortType)))
            }
        notifyListeners()
    }

    private fun applyFavoritesFilter(notes: List<Note>): List<Note> {
        val favoritesFilterManager = FavoritesFilterStateManager.getInstance()
        return if (favoritesFilterManager.isFilteringFavorites()) {
            notes.filter { it.isFavorite }
        } else {
            notes
        }
    }

    private fun applySearchFilters(notes: List<Note>): List<Note> {
        val filterStateManager = SearchFilterStateManager.getInstance()
        return noteFilterService.applyFilters(
            notes,
            filterStateManager.getActiveDateFilter(),
            filterStateManager.isFavoriteFilterActive(),
            filterStateManager.getActiveColorFilters(),
        )
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
