package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.ZipHelper
import com.nazarethlabs.codex.service.sentry.SentryEventHelper
import java.io.File

class ExportNotesService {
    fun export(
        notes: List<Note>,
        outputPath: String,
    ): File {
        val filePaths = notes.map { it.filePath }
        val zipPath = ZipHelper.createZipFromFiles(filePaths, outputPath)
        SentryEventHelper.captureEvent("note.exported")
        return File(zipPath)
    }
}
