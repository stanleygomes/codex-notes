package com.nazarethlabs.codex.ui.component

import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Color.LIGHT_GRAY
import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Insets
import java.awt.RenderingHints.KEY_ANTIALIASING
import java.awt.RenderingHints.VALUE_ANTIALIAS_ON
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.border.Border

class ButtonComponent {
    fun build(
        icon: Icon,
        tooltip: String? = null,
    ): JButton {
        val button = JButton(icon)

        button.apply {
            if (tooltip != null) toolTipText = tooltip

            preferredSize = Dimension(JBUI.scale(28), JBUI.scale(28))
            minimumSize = preferredSize
            maximumSize = preferredSize
            border = RoundedBorder(JBColor.border(), 0, 5)

            addMouseListener(
                object : MouseAdapter() {
                    override fun mouseEntered(e: MouseEvent?) {
                        border = RoundedBorder(LIGHT_GRAY, 1, 5)
                        repaint()
                    }

                    override fun mouseExited(e: MouseEvent?) {
                        border = RoundedBorder(LIGHT_GRAY, 0, 5)
                        repaint()
                    }

                    override fun mousePressed(e: MouseEvent?) {
                        border = RoundedBorder(LIGHT_GRAY, 2, 5)
                        repaint()
                    }

                    override fun mouseReleased(e: MouseEvent?) {
                        border = RoundedBorder(LIGHT_GRAY, 1, 5)
                        repaint()
                    }
                },
            )
        }

        return button
    }

    private class RoundedBorder(
        private val color: Color,
        private val thickness: Int,
        private val radius: Int,
    ) : Border {
        override fun getBorderInsets(c: Component?): Insets =
            Insets(
                thickness + radius,
                thickness + radius,
                thickness + radius,
                thickness + radius,
            )

        override fun isBorderOpaque(): Boolean = false

        override fun paintBorder(
            c: Component?,
            g: Graphics?,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
        ) {
            if (thickness == 0) return
            val g2 = g as Graphics2D
            g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
            g2.color = color
            g2.stroke = BasicStroke(thickness.toFloat())
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness, radius * 2, radius * 2)
        }
    }
}
