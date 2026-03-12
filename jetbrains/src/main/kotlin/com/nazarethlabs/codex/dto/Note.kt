package com.nazarethlabs.codex.dto

import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.enum.NoteColorEnum.NONE

data class Note(
    var id: String = "",
    var title: String = "",
    var filePath: String = "",
    var createdAt: Long = 0L,
    var updatedAt: Long = 0L,
    var isFavorite: Boolean = false,
    var color: NoteColorEnum = NONE,
)
