package com.nazarethlabs.codex.service.note

import com.intellij.openapi.diagnostic.logger
import com.nazarethlabs.codex.repository.NoteLegacyStorageRepository
import com.nazarethlabs.codex.repository.NoteStorageRepository

class NoteMigrationService {
    private val log = logger<NoteMigrationService>()

    fun migrateIfNeeded() {
        try {
            val legacyRepository = NoteLegacyStorageRepository.getInstance()
            val storageRepository = NoteStorageRepository.getInstance()

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
