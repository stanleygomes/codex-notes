package com.nazarethlabs.codex.state

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.nazarethlabs.codex.listener.FavoritesFilterStateListener

@Service(Service.Level.APP)
class FavoritesFilterStateManager {
    private val listeners = mutableListOf<FavoritesFilterStateListener>()
    private var isFilteringFavorites = false

    fun toggleFavoritesFilter() {
        isFilteringFavorites = !isFilteringFavorites
        notifyListeners()
        NotesStateManager.getInstance().refresh()
    }

    fun isFilteringFavorites(): Boolean = isFilteringFavorites

    fun addListener(listener: FavoritesFilterStateListener) {
        listeners.add(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener.onFavoritesFilterChanged(isFilteringFavorites)
        }
    }

    companion object {
        fun getInstance(): FavoritesFilterStateManager = service()
    }
}
