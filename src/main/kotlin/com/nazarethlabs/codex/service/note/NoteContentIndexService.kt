package com.nazarethlabs.codex.service.note

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.nazarethlabs.codex.dto.Note
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Service(Service.Level.APP)
class NoteContentIndexService {
    private val contentCache = ConcurrentHashMap<String, CachedContent>()

    fun getContent(note: Note): String? {
        val file = File(note.filePath)
        if (!file.exists() || !file.isFile) {
            contentCache.remove(note.id)
            return null
        }

        val lastModified = file.lastModified()
        val cached = contentCache[note.id]

        if (cached != null && cached.lastModified == lastModified) {
            return cached.content
        }

        return readAndCacheContent(note.id, file, lastModified)
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
        file: File,
        lastModified: Long,
    ): String? =
        try {
            val content = file.readText()
            contentCache[noteId] = CachedContent(content, lastModified)
            content
        } catch (e: Exception) {
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
