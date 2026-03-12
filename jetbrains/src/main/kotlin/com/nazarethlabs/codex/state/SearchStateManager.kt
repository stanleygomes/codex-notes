package com.nazarethlabs.codex.state

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.nazarethlabs.codex.listener.SearchStateListener

@Service(Service.Level.APP)
class SearchStateManager {
    private val listeners = mutableListOf<SearchStateListener>()
    private var isSearchVisible = false

    fun toggleSearchVisibility() {
        isSearchVisible = !isSearchVisible
        notifyListeners()

        if (!isSearchVisible) {
            NotesStateManager.getInstance().clearSearch()
        }
    }

    fun addListener(listener: SearchStateListener) {
        listeners.add(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener.onSearchVisibilityChanged(isSearchVisible)
        }
    }

    companion object {
        fun getInstance(): SearchStateManager = service()
    }
}
