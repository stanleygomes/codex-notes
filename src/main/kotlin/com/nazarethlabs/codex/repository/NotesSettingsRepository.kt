package com.nazarethlabs.codex.repository

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.nazarethlabs.codex.enum.SortTypeEnum
import com.nazarethlabs.codex.state.ConfigurationState

@Service(Service.Level.APP)
@State(
    name = "NotesSettingsState",
    storages = [Storage("notesSettings.xml")],
)
class NotesSettingsRepository : PersistentStateComponent<ConfigurationState> {
    private var state = ConfigurationState()

    override fun getState(): ConfigurationState = state

    override fun loadState(state: ConfigurationState) {
        this.state = state
    }

    companion object {
        fun getInstance(): NotesSettingsRepository = ApplicationManager.getApplication().getService(NotesSettingsRepository::class.java)
    }

    fun getDefaultFileExtension(): String = state.configuration.defaultFileExtension

    fun setDefaultFileExtension(extension: String) {
        state.configuration.defaultFileExtension = extension
    }

    fun getDefaultSortType(): SortTypeEnum = state.configuration.defaultSortType

    fun setDefaultSortType(sortType: SortTypeEnum) {
        state.configuration.defaultSortType = sortType
    }
}
