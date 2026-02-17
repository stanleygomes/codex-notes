package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.OpenFolderHelper

class OpenNoteFileLocationService {
    fun openFileLocation(note: Note) {
        val parentPath = FileHelper.getParentPath(note.filePath)
        OpenFolderHelper.openFolder(parentPath)
    }
}
