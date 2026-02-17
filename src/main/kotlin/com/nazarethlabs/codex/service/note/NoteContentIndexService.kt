package com.nazarethlabs.codex.service.note

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.FileHelper
import java.util.concurrent.ConcurrentHashMap

@Service(Service.Level.APP)
class NoteContentIndexService {
    private val contentCache = ConcurrentHashMap<String, CachedContent>()

    fun getContent(note: Note): String? {
        if (!FileHelper.isFile(note.filePath)) {
            contentCache.remove(note.id)
            return null
        }

        val lastModified = FileHelper.getLastModified(note.filePath)
        val cached = contentCache[note.id]

        if (cached != null && cached.lastModified == lastModified) {
            return cached.content
        }

        return readAndCacheContent(note.id, note.filePath, lastModified)
    }

    fun getContentForSearch(notes: List<Note>): Map<String, String> =
        notes.associate { note ->
            note.id to (getContent(note) ?: "")
        }

    fun invalidateCache(noteId: String) {
        contentCache.remove(noteId)
    }

    fun invalidateAllCache() {
        contentCache.clear()
    }

    private fun readAndCacheContent(
        noteId: String,
        filePath: String,
        lastModified: Long,
    ): String? =
        try {
            val content = FileHelper.readText(filePath)
            contentCache[noteId] = CachedContent(content, lastModified)
            content
        } catch (_: Exception) {
            contentCache.remove(noteId)
            null
        }

    private data class CachedContent(
        val content: String,
        val lastModified: Long,
    )

    companion object {
        fun getInstance(): NoteContentIndexService = ApplicationManager.getApplication().getService(NoteContentIndexService::class.java)
    }
}
