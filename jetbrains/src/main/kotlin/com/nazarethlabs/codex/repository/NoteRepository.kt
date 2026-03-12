package com.nazarethlabs.codex.repository

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.ProjectManager
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.helper.DatabaseHelper
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.listener.NoteEventListener
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.UUID

@Service(Service.Level.APP)
class NoteRepository {
    private val databasePath: String by lazy {
        val notesDir = resolveNotesDirectory()
        FileHelper.buildPath(notesDir, "data", "notes.db")
    }

    private val jdbcUrl: String by lazy {
        "jdbc:sqlite:$databasePath"
    }

    @Volatile
    private var initialized = false

    companion object {
        private const val TABLE_NAME = "notes"

        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_FILE_PATH = "filePath"
        private const val COLUMN_CREATED_AT = "createdAt"
        private const val COLUMN_UPDATED_AT = "updatedAt"
        private const val COLUMN_IS_FAVORITE = "isFavorite"
        private const val COLUMN_COLOR = "color"

        private const val SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_FILE_PATH TEXT NOT NULL,
                $COLUMN_CREATED_AT INTEGER NOT NULL,
                $COLUMN_UPDATED_AT INTEGER NOT NULL,
                $COLUMN_IS_FAVORITE INTEGER NOT NULL DEFAULT 0,
                $COLUMN_COLOR TEXT NOT NULL DEFAULT 'NONE'
            )
        """

        private const val SQL_INSERT = """
            INSERT INTO $TABLE_NAME (
                $COLUMN_ID, $COLUMN_TITLE, $COLUMN_FILE_PATH, 
                $COLUMN_CREATED_AT, $COLUMN_UPDATED_AT, 
                $COLUMN_IS_FAVORITE, $COLUMN_COLOR
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """

        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?"

        private const val SQL_SELECT_ALL = """
            SELECT $COLUMN_ID, $COLUMN_TITLE, $COLUMN_FILE_PATH, 
                   $COLUMN_CREATED_AT, $COLUMN_UPDATED_AT, 
                   $COLUMN_IS_FAVORITE, $COLUMN_COLOR 
            FROM $TABLE_NAME
        """

        private const val SQL_EXISTS = "SELECT COUNT(*) FROM $TABLE_NAME WHERE $COLUMN_ID = ?"

        private const val SQL_TOGGLE_FAVORITE = """
            UPDATE $TABLE_NAME 
            SET $COLUMN_IS_FAVORITE = 1 - $COLUMN_IS_FAVORITE, $COLUMN_UPDATED_AT = ? 
            WHERE $COLUMN_ID = ?
        """

        private const val SQL_CHANGE_COLOR = """
            UPDATE $TABLE_NAME 
            SET $COLUMN_COLOR = ?, $COLUMN_UPDATED_AT = ? 
            WHERE $COLUMN_ID = ?
        """

        fun getInstance(): NoteRepository = ApplicationManager.getApplication().getService(NoteRepository::class.java)
    }

    private fun resolveNotesDirectory(): String =
        try {
            NotesSettingsService().getNotesDirectory()
        } catch (_: Exception) {
            FileHelper.getDefaultNotesDir()
        }

    @Synchronized
    private fun ensureInitialized() {
        if (!initialized) {
            DatabaseHelper.ensureDriverLoaded()
            FileHelper.ensureDirectoryExists(FileHelper.getParentPath(databasePath))
            createTable()
            initialized = true
        }
    }

    private fun <T> withConnection(block: (Connection) -> T): T {
        ensureInitialized()
        return try {
            DriverManager.getConnection(jdbcUrl).use(block)
        } catch (e: SQLException) {
            throw RuntimeException("Database operation failed: ${e.message}", e)
        }
    }

    private fun createTable() {
        DriverManager.getConnection(jdbcUrl).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(SQL_CREATE_TABLE)
            }
        }
    }

    private fun insertNote(note: Note) {
        withConnection { conn ->
            conn.prepareStatement(SQL_INSERT).use { stmt ->
                stmt.setString(1, note.id)
                stmt.setString(2, note.title)
                stmt.setString(3, note.filePath)
                stmt.setLong(4, note.createdAt)
                stmt.setLong(5, note.updatedAt)
                stmt.setInt(6, if (note.isFavorite) 1 else 0)
                stmt.setString(7, note.color.name)
                stmt.executeUpdate()
            }
        }
    }

    private fun updateNoteInDb(
        id: String,
        title: String?,
        filePath: String? = null,
    ) {
        withConnection { conn ->
            val setClauses = mutableListOf<String>()
            if (title != null) setClauses.add("$COLUMN_TITLE = ?")
            if (filePath != null) setClauses.add("$COLUMN_FILE_PATH = ?")
            setClauses.add("$COLUMN_UPDATED_AT = ?")

            val sql = "UPDATE $TABLE_NAME SET ${setClauses.joinToString(", ")} WHERE $COLUMN_ID = ?"

            conn.prepareStatement(sql).use { stmt ->
                var paramIndex = 1
                if (title != null) stmt.setString(paramIndex++, title)
                if (filePath != null) stmt.setString(paramIndex++, filePath)
                stmt.setLong(paramIndex++, System.currentTimeMillis())
                stmt.setString(paramIndex, id)
                stmt.executeUpdate()
            }
        }
    }

    private fun toggleFavoriteInDb(id: String) {
        withConnection { conn ->
            conn.prepareStatement(SQL_TOGGLE_FAVORITE).use { stmt ->
                stmt.setLong(1, System.currentTimeMillis())
                stmt.setString(2, id)
                stmt.executeUpdate()
            }
        }
    }

    private fun changeColorInDb(
        id: String,
        color: NoteColorEnum,
    ) {
        withConnection { conn ->
            conn.prepareStatement(SQL_CHANGE_COLOR).use { stmt ->
                stmt.setString(1, color.name)
                stmt.setLong(2, System.currentTimeMillis())
                stmt.setString(3, id)
                stmt.executeUpdate()
            }
        }
    }

    private fun deleteNoteFromDb(id: String) {
        withConnection { conn ->
            conn.prepareStatement(SQL_DELETE).use { stmt ->
                stmt.setString(1, id)
                stmt.executeUpdate()
            }
        }
    }

    private fun getAllNotesFromDb(): List<Note> =
        withConnection { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(SQL_SELECT_ALL)
                val notes = mutableListOf<Note>()
                while (rs.next()) {
                    notes.add(
                        Note(
                            id = rs.getString(COLUMN_ID),
                            title = rs.getString(COLUMN_TITLE),
                            filePath = rs.getString(COLUMN_FILE_PATH),
                            createdAt = rs.getLong(COLUMN_CREATED_AT),
                            updatedAt = rs.getLong(COLUMN_UPDATED_AT),
                            isFavorite = rs.getInt(COLUMN_IS_FAVORITE) == 1,
                            color =
                                try {
                                    NoteColorEnum.valueOf(rs.getString(COLUMN_COLOR))
                                } catch (_: IllegalArgumentException) {
                                    NoteColorEnum.NONE
                                },
                        ),
                    )
                }
                notes
            }
        }

    private fun noteExists(id: String): Boolean =
        withConnection { conn ->
            conn.prepareStatement(SQL_EXISTS).use { stmt ->
                stmt.setString(1, id)
                val rs = stmt.executeQuery()
                rs.next() && rs.getInt(1) > 0
            }
        }

    fun addNote(
        title: String,
        filePath: String,
    ): Note {
        val note =
            Note(
                id = UUID.randomUUID().toString(),
                title = title,
                filePath = filePath,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
            )

        insertNote(note)

        notifyAllProjects { it.notifyNoteCreated() }

        return note
    }

    fun updateNote(
        id: String,
        title: String? = null,
        filePath: String? = null,
    ) {
        updateNoteInDb(id, title, filePath)

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun toggleFavorite(id: String) {
        toggleFavoriteInDb(id)

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun changeColor(
        id: String,
        color: NoteColorEnum,
    ) {
        changeColorInDb(id, color)

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun removeNote(id: String) {
        deleteNoteFromDb(id)

        notifyAllProjects { it.notifyNoteDeleted() }
    }

    fun getAllNotes(): List<Note> = getAllNotesFromDb()

    fun importNote(note: Note) {
        if (!noteExists(note.id)) {
            insertNote(note)
        }
    }

    private fun notifyAllProjects(action: (NoteEventListener) -> Unit) {
        ProjectManager.getInstance().openProjects.forEach { project ->
            action(NoteEventListener.getInstance(project))
        }
    }
}
