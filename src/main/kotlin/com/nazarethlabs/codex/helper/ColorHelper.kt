package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum.NONE
import java.awt.Color
import java.awt.Color.BLACK
import java.awt.Color.WHITE
import javax.swing.JList

class ColorHelper {
    fun calculateColors(
        theList: JList<out Note>?,
        note: Note,
        isSelected: Boolean,
    ): Pair<Color, Color> {
        val backgroundColor =
            if (isSelected) {
                theList?.selectionBackground ?: Color.GRAY
            } else {
                if (note.color != NONE) note.color.color else theList?.background ?: WHITE
            }

        val foregroundColor =
            if (isSelected) {
                theList?.selectionForeground ?: WHITE
            } else {
                val bgColor = if (note.color != NONE) note.color.color else theList?.background ?: WHITE
                if (isBackgroundTooLight(bgColor)) BLACK else theList?.foreground ?: BLACK
            }

        return Pair(backgroundColor, foregroundColor)
    }

    fun calculateDateLabelColor(
        theList: JList<out Note>?,
        note: Note,
        isSelected: Boolean,
    ): Color =
        if (isSelected) {
            (theList?.selectionForeground ?: WHITE).darker()
        } else {
            val bgColor = if (note.color != NONE) note.color.color else theList?.background ?: WHITE
            if (isBackgroundTooLight(bgColor)) {
                Color(64, 64, 64)
            } else {
                Color(128, 128, 128)
            }
        }

    private fun isBackgroundTooLight(color: Color): Boolean {
        val brightness = (color.red * 0.299 + color.green * 0.587 + color.blue * 0.114)
        return brightness > 186
    }
}
