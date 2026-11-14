package com.nazarethlabs.codex.service.note

import com.intellij.openapi.diagnostic.logger
import com.nazarethlabs.codex.repository.NoteLegacyStorageRepository
import com.nazarethlabs.codex.repository.NoteRepository

class NoteMigrationLegacyService {
    private val log = logger<NoteMigrationLegacyService>()

    fun migrateIfNeeded() {
        try {
            val legacyRepository = NoteLegacyStorageRepository.getInstance()
            val storageRepository = NoteRepository.getInstance()

            val legacyNotes = legacyRepository.getAllNotes()
            if (legacyNotes.isEmpty()) return

            log.info("Migrating ${legacyNotes.size} notes from legacy storage to SQLite")

            legacyNotes.forEach { note ->
                storageRepository.importNote(note)
            }

            legacyRepository.clearNotes()

            log.info("Migration completed successfully")
        } catch (e: Exception) {
            log.error("Failed to migrate notes from legacy storage to SQLite", e)
        }
    }
}
