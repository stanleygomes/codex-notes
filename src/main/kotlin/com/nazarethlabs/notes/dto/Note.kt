package com.nazarethlabs.notes.dto

import com.nazarethlabs.notes.enum.NoteColorEnum
import com.nazarethlabs.notes.enum.NoteColorEnum.NONE

data class Note(
    var id: String = "",
    var title: String = "",
    var filePath: String = "",
    var createdAt: Long = 0L,
    var updatedAt: Long = 0L,
    var isFavorite: Boolean = false,
    var color: NoteColorEnum = NONE,
)
