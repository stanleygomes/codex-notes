package com.nazarethlabs.codex.search

import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributor
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributorFactory
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.util.Processor
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.repository.NoteStorageRepository
import java.io.File
import javax.swing.ListCellRenderer

class NotesSearchEverywhereContributorFactory : SearchEverywhereContributorFactory<Note> {
    override fun createContributor(initEvent: AnActionEvent): SearchEverywhereContributor<Note> =
        NotesSearchEverywhereContributor(initEvent)
}

class NotesSearchEverywhereContributor(
    private val event: AnActionEvent,
) : SearchEverywhereContributor<Note> {
    private val noteStorage =
        ApplicationManager
            .getApplication()
            .getService(NoteStorageRepository::class.java)

    override fun getSearchProviderId(): String = "NotesSearchEverywhereContributor"

    override fun getGroupName(): String = "Notes"

    override fun getSortWeight(): Int = 300

    override fun showInFindResults(): Boolean = true

    override fun fetchElements(
        pattern: String,
        progressIndicator: ProgressIndicator,
        consumer: Processor<in Note>,
    ) {
        if (pattern.isEmpty()) return

        val notes = noteStorage.getAllNotes()
        val lowerPattern = pattern.lowercase()

        notes.forEach { note ->
            if (progressIndicator.isCanceled) return

            val titleMatches = note.title.lowercase().contains(lowerPattern)

            val contentMatches = searchInNoteContent(note, lowerPattern)

            if (titleMatches || contentMatches) {
                consumer.process(note)
            }
        }
    }

    private fun searchInNoteContent(
        note: Note,
        pattern: String,
    ): Boolean {
        return try {
            val file = File(note.filePath)
            if (!file.exists()) return false

            val content = file.readText().lowercase()
            content.contains(pattern)
        } catch (e: Exception) {
            false
        }
    }

    override fun getElementsRenderer(): ListCellRenderer<in Note> = NoteSearchCellRenderer()

    override fun getDataForItem(
        element: Note,
        dataId: String,
    ): Any? = null

    override fun processSelectedItem(
        selected: Note,
        modifiers: Int,
        searchText: String,
    ): Boolean {
        event.project?.let { project ->
            val file = File(selected.filePath)
            if (file.exists()) {
                com.intellij.openapi.fileEditor.FileEditorManager
                    .getInstance(project)
                    .openFile(
                        com.intellij.openapi.vfs.LocalFileSystem
                            .getInstance()
                            .findFileByPath(selected.filePath) ?: return false,
                        true,
                    )
                return true
            }
        }
        return false
    }
}
