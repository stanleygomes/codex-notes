package com.nazarethlabs.codex.repository

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class NoteDatabaseManager(
    private val databaseDir: String,
) {
    private val databasePath = File(databaseDir, "notes.db").absolutePath
    private var initialized = false

    @Synchronized
    private fun ensureInitialized() {
        if (!initialized) {
            File(databaseDir).mkdirs()
            createTable()
            initialized = true
        }
    }

    private fun <T> withConnection(block: (Connection) -> T): T {
        ensureInitialized()
        return DriverManager.getConnection("jdbc:sqlite:$databasePath").use(block)
    }

    private fun createTable() {
        DriverManager.getConnection("jdbc:sqlite:$databasePath").use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(
                    """
                    CREATE TABLE IF NOT EXISTS notes (
                        id TEXT PRIMARY KEY,
                        title TEXT NOT NULL,
                        filePath TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        isFavorite INTEGER NOT NULL DEFAULT 0,
                        color TEXT NOT NULL DEFAULT 'NONE'
                    )
                    """.trimIndent(),
                )
            }
        }
    }

    fun insertNote(note: Note) {
        withConnection { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO notes (id, title, filePath, createdAt, updatedAt, isFavorite, color) VALUES (?, ?, ?, ?, ?, ?, ?)",
                ).use { stmt ->
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

    fun updateNote(
        id: String,
        title: String?,
        filePath: String? = null,
    ) {
        withConnection { conn ->
            val setClauses = mutableListOf<String>()
            if (title != null) setClauses.add("title = ?")
            if (filePath != null) setClauses.add("filePath = ?")
            setClauses.add("updatedAt = ?")

            val sql = "UPDATE notes SET ${setClauses.joinToString(", ")} WHERE id = ?"

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

    fun toggleFavorite(id: String) {
        withConnection { conn ->
            conn
                .prepareStatement("UPDATE notes SET isFavorite = 1 - isFavorite, updatedAt = ? WHERE id = ?")
                .use { stmt ->
                    stmt.setLong(1, System.currentTimeMillis())
                    stmt.setString(2, id)
                    stmt.executeUpdate()
                }
        }
    }

    fun changeColor(
        id: String,
        color: NoteColorEnum,
    ) {
        withConnection { conn ->
            conn
                .prepareStatement("UPDATE notes SET color = ?, updatedAt = ? WHERE id = ?")
                .use { stmt ->
                    stmt.setString(1, color.name)
                    stmt.setLong(2, System.currentTimeMillis())
                    stmt.setString(3, id)
                    stmt.executeUpdate()
                }
        }
    }

    fun deleteNote(id: String) {
        withConnection { conn ->
            conn.prepareStatement("DELETE FROM notes WHERE id = ?").use { stmt ->
                stmt.setString(1, id)
                stmt.executeUpdate()
            }
        }
    }

    fun getAllNotes(): List<Note> =
        withConnection { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery("SELECT id, title, filePath, createdAt, updatedAt, isFavorite, color FROM notes")
                val notes = mutableListOf<Note>()
                while (rs.next()) {
                    notes.add(
                        Note(
                            id = rs.getString("id"),
                            title = rs.getString("title"),
                            filePath = rs.getString("filePath"),
                            createdAt = rs.getLong("createdAt"),
                            updatedAt = rs.getLong("updatedAt"),
                            isFavorite = rs.getInt("isFavorite") == 1,
                            color =
                                try {
                                    NoteColorEnum.valueOf(rs.getString("color"))
                                } catch (_: IllegalArgumentException) {
                                    NoteColorEnum.NONE
                                },
                        ),
                    )
                }
                notes
            }
        }

    fun noteExists(id: String): Boolean =
        withConnection { conn ->
            conn.prepareStatement("SELECT COUNT(*) FROM notes WHERE id = ?").use { stmt ->
                stmt.setString(1, id)
                val rs = stmt.executeQuery()
                rs.next() && rs.getInt(1) > 0
            }
        }
}
