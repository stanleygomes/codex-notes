package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import javax.swing.Icon
import javax.swing.JList

class ColorHelper {
    fun calculateColors(
        theList: JList<out Note>?,
        isSelected: Boolean,
    ): Pair<Color, Color> {
        val backgroundColor =
            if (isSelected) {
                theList!!.selectionBackground
            } else {
                theList!!.background
            }

        val foregroundColor =
            if (isSelected) {
                theList.selectionForeground
            } else {
                theList.foreground
            }

        return Pair(backgroundColor, foregroundColor)
    }

    fun calculateSecondaryColor(
        theList: JList<out Note>?,
        isSelected: Boolean,
    ): Color =
        if (isSelected) {
            theList!!.selectionForeground.darker()
        } else {
            if (isBackgroundTooLight(theList!!.background)) {
                Color(120, 120, 120)
            } else {
                Color(150, 150, 150)
            }
        }

    private fun isBackgroundTooLight(color: Color): Boolean {
        val brightness = (color.red * 0.299 + color.green * 0.587 + color.blue * 0.114)
        return brightness > 186
    }

    fun createColorIcon(color: Color): Icon =
        object : Icon {
            override fun paintIcon(
                c: Component?,
                g: Graphics?,
                x: Int,
                y: Int,
            ) {
                g?.color = color
                g?.fillRect(x, y, iconWidth, iconHeight)
            }

            override fun getIconWidth(): Int = 16

            override fun getIconHeight(): Int = 16
        }
}
