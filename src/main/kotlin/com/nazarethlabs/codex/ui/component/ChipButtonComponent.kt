package com.nazarethlabs.codex.ui.component

import com.intellij.util.ui.JBUI
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JToggleButton
import javax.swing.UIManager

class ChipButtonComponent {
    fun build(
        text: String,
        onToggle: (Boolean) -> Unit,
    ): JToggleButton = createChipButton(text, null, onToggle)

    fun buildColorChip(
        chipColor: Color,
        tooltip: String,
        onToggle: (Boolean) -> Unit,
    ): JToggleButton =
        createChipButton(null, chipColor, onToggle).apply {
            toolTipText = tooltip
        }

    private fun createChipButton(
        text: String?,
        chipColor: Color?,
        onToggle: (Boolean) -> Unit,
    ): JToggleButton {
        val chip =
            object : JToggleButton(text ?: "") {
                override fun paintComponent(g: Graphics) {
                    val g2 = g as Graphics2D
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

                    val arc = JBUI.scale(12)
                    val bgColor = resolveBackgroundColor(chipColor)
                    g2.color = bgColor
                    g2.fillRoundRect(0, 0, width, height, arc, arc)

                    if (isSelected) {
                        g2.color = resolveBorderColor()
                        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc)
                    }

                    if (text != null && getText().isNotEmpty()) {
                        g2.color = resolveForegroundColor()
                        val fm = g2.fontMetrics
                        val textX = (width - fm.stringWidth(getText())) / 2
                        val textY = (height + fm.ascent - fm.descent) / 2
                        g2.drawString(getText(), textX, textY)
                    }
                }

                private fun resolveBackgroundColor(chipColor: Color?): Color =
                    if (chipColor != null) {
                        if (isSelected) chipColor.darker() else chipColor
                    } else if (isSelected) {
                        UIManager.getColor("Button.default.startBackground")
                            ?: Color(70, 130, 180)
                    } else {
                        UIManager.getColor("Button.background")
                            ?: Color(220, 220, 220)
                    }

                private fun resolveForegroundColor(): Color =
                    if (isSelected) {
                        Color.WHITE
                    } else {
                        UIManager.getColor("Button.foreground")
                            ?: Color.BLACK
                    }

                private fun resolveBorderColor(): Color =
                    UIManager.getColor("Button.default.focusedBorderColor")
                        ?: Color(100, 150, 200)
            }

        chip.apply {
            isContentAreaFilled = false
            isBorderPainted = false
            isFocusPainted = false
            isOpaque = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            font = JBUI.Fonts.smallFont()

            if (chipColor != null && text == null) {
                preferredSize = Dimension(JBUI.scale(24), JBUI.scale(22))
            } else {
                val fm = getFontMetrics(font)
                val textWidth = fm.stringWidth(getText())
                preferredSize = Dimension(textWidth + JBUI.scale(20), JBUI.scale(22))
            }

            addMouseListener(
                object : MouseAdapter() {
                    override fun mouseEntered(e: MouseEvent?) {
                        repaint()
                    }

                    override fun mouseExited(e: MouseEvent?) {
                        repaint()
                    }
                },
            )

            addActionListener {
                onToggle(isSelected)
                repaint()
            }
        }

        return chip
    }
}
