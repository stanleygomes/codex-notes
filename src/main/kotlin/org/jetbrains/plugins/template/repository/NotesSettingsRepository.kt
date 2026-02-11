package org.jetbrains.plugins.template.repository

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Configuration
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
        return state.configurations.firstOrNull()?.defaultFileExtension ?: ".md"
    }

    fun setDefaultFileExtension(extension: String) {
        val config = state.configurations.firstOrNull() ?: Configuration()
        config.defaultFileExtension = extension
        if (state.configurations.isEmpty()) {
            state.configurations.add(config)
        }
    }
}
