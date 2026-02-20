package com.nazarethlabs.codex.editor.extension

import com.intellij.openapi.fileEditor.impl.NonProjectFileWritingAccessExtension
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.service.settings.NotesSettingsService

class NoteFileWritingAccessExtension : NonProjectFileWritingAccessExtension {
    override fun isWritable(file: VirtualFile): Boolean = isNoteFile(file)

    private fun isNoteFile(file: VirtualFile): Boolean {
        val notesDirectory = NotesSettingsService().getNotesDirectory()
        return file.path.startsWith(notesDirectory)
    }
}
