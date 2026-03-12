package com.nazarethlabs.codex.enum

import java.awt.Color

enum class NoteColorEnum(
    val displayNameKey: String,
    val color: Color,
) {
    NONE("note.color.none", Color(0, 0, 0, 0)),
    BLUE("note.color.blue", Color(173, 216, 230)),
    GREEN("note.color.green", Color(144, 238, 144)),
    YELLOW("note.color.yellow", Color(255, 255, 153)),
    ORANGE("note.color.orange", Color(255, 218, 185)),
    PINK("note.color.pink", Color(255, 182, 193)),
    RED("note.color.red", Color(255, 102, 102)),
    PURPLE("note.color.purple", Color(204, 153, 255)),
}
