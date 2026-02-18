package com.nazarethlabs.codex.service.note

import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import java.io.File

class ImportNotesService {
    private val createNoteService = CreateNoteService()

    fun importFiles(files: List<File>): List<VirtualFile> {
        val importedFiles = mutableListOf<VirtualFile>()
        for (file in files) {
            if (file.exists() && file.isFile) {
                val content = file.readText()
                val title = resolveUniqueTitle(file.name)
                val extension = getExtension(file)
                val virtualFile = createNoteService.createWithContent(null, title, extension, content, false)
                if (virtualFile != null) {
                    importedFiles.add(virtualFile)
                }
            }
        }

        return importedFiles
    }

    private fun resolveUniqueTitle(originalName: String): String {
        val notes = NoteStorageRepository.getInstance().getAllNotes()
        val title = originalName.substringBeforeLast(".")

        if (notes.none { it.title == title }) {
            return title
        }

        var counter = 1
        var candidate: String
        do {
            candidate = "$title ($counter)"
            counter++
        } while (notes.any { it.title == candidate })

        return candidate
    }

    private fun getExtension(file: File): String {
        val extension = file.extension
        return if (extension.isNotEmpty()) ".$extension" else NotesSettingsService().getDefaultFileExtension()
    }
}
