package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum.NONE
import java.awt.Color
import java.awt.Color.BLACK
import javax.swing.JList

class ColorHelper {
    fun calculateColors(
        theList: JList<out Note>?,
        note: Note,
        isSelected: Boolean,
    ): Pair<Color, Color> {
        val backgroundColor =
            if (isSelected) {
                theList!!.selectionBackground
            } else {
                if (note.color != NONE) note.color.color else theList!!.background
            }

        val foregroundColor =
            if (isSelected) {
                theList!!.selectionForeground
            } else {
                val bgColor = if (note.color != NONE) note.color.color else theList!!.background
                if (isBackgroundTooLight(bgColor)) BLACK else theList!!.foreground
            }

        return Pair(backgroundColor, foregroundColor)
    }

    fun calculateDateLabelColor(
        theList: JList<out Note>?,
        note: Note,
        isSelected: Boolean,
    ): Color =
        if (isSelected) {
            // When selected, use a slightly dimmed version of selection foreground
            theList!!.selectionForeground.darker()
        } else {
            val bgColor = if (note.color != NONE) note.color.color else theList!!.background
            if (isBackgroundTooLight(bgColor)) {
                // Dark background for date when main background is light
                Color(64, 64, 64) // Dark gray
            } else {
                // Light gray for date when main background is dark
                Color(128, 128, 128)
            }
        }

    private fun isBackgroundTooLight(color: Color): Boolean {
        // Calculate the brightness of the color using the luminance formula
        val brightness = (color.red * 0.299 + color.green * 0.587 + color.blue * 0.114)
        // If brightness is greater than 186, the color is considered too light
        return brightness > 186
    }
}
