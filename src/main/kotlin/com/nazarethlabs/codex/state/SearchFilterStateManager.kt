package com.nazarethlabs.codex.state

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.nazarethlabs.codex.enum.DateFilterEnum
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.listener.SearchFilterStateListener

@Service(Service.Level.APP)
class SearchFilterStateManager {
    private val listeners = mutableListOf<SearchFilterStateListener>()
    private var activeDateFilter: DateFilterEnum? = null
    private var isFavoriteFilterActive = false
    private val activeColorFilters = mutableSetOf<NoteColorEnum>()

    fun toggleDateFilter(dateFilter: DateFilterEnum) {
        activeDateFilter = if (activeDateFilter == dateFilter) null else dateFilter
        notifyListenersAndRefresh()
    }

    fun toggleFavoriteFilter() {
        isFavoriteFilterActive = !isFavoriteFilterActive
        notifyListenersAndRefresh()
    }

    fun toggleColorFilter(color: NoteColorEnum) {
        if (activeColorFilters.contains(color)) {
            activeColorFilters.remove(color)
        } else {
            activeColorFilters.add(color)
        }
        notifyListenersAndRefresh()
    }

    fun getActiveDateFilter(): DateFilterEnum? = activeDateFilter

    fun isFavoriteFilterActive(): Boolean = isFavoriteFilterActive

    fun getActiveColorFilters(): Set<NoteColorEnum> = activeColorFilters.toSet()

    fun hasActiveFilters(): Boolean = activeDateFilter != null || isFavoriteFilterActive || activeColorFilters.isNotEmpty()

    fun clearAllFilters() {
        activeDateFilter = null
        isFavoriteFilterActive = false
        activeColorFilters.clear()
        notifyListenersAndRefresh()
    }

    fun addListener(listener: SearchFilterStateListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: SearchFilterStateListener) {
        listeners.remove(listener)
    }

    private fun notifyListenersAndRefresh() {
        listeners.forEach { it.onFilterStateChanged() }
        NotesStateManager.getInstance().refresh()
    }

    companion object {
        fun getInstance(): SearchFilterStateManager = service()
    }
}
