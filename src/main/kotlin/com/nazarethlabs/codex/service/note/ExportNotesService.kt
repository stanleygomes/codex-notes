package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.ZipHelper
import java.io.File

class ExportNotesService {
    fun export(
        notes: List<Note>,
        outputPath: String,
    ): File {
        val noteFiles = notes.map { File(it.filePath) }
        return ZipHelper.createZipFromFiles(noteFiles, outputPath)
    }
}
