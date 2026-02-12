package org.jetbrains.plugins.template.repository

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.enum.SortTypeEnum
import org.jetbrains.plugins.template.state.ConfigurationState

@Service(Service.Level.PROJECT)
@State(
    name = "NotesSettingsState",
    storages = [Storage("notesSettings.xml")]
)
class NotesSettingsRepository : PersistentStateComponent<ConfigurationState> {

    private var state = ConfigurationState()

    override fun getState(): ConfigurationState = state

    override fun loadState(state: ConfigurationState) {
        this.state = state
    }

    companion object {
        fun getInstance(project: Project): NotesSettingsRepository {
            return project.getService(NotesSettingsRepository::class.java)
        }
    }

    fun getDefaultFileExtension(): String {
        return state.configuration.defaultFileExtension!!
    }

    fun setDefaultFileExtension(extension: String) {
        state.configuration.defaultFileExtension = extension
    }

    fun getDefaultSortType(): SortTypeEnum {
        return state.configuration.defaultSortType
    }

    fun setDefaultSortType(sortType: SortTypeEnum) {
        state.configuration.defaultSortType = sortType
    }
}
