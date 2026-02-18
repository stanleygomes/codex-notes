package com.nazarethlabs.codex.ui.search

import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.enum.DateFilterEnum
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.enum.NoteColorEnum.NONE
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.listener.SearchFilterStateListener
import com.nazarethlabs.codex.state.SearchFilterStateManager
import com.nazarethlabs.codex.ui.component.ChipButtonComponent
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.FlowLayout.LEFT
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JToggleButton

class SearchFilterComponent : SearchFilterStateListener {
    private val dateButtons = mutableMapOf<DateFilterEnum, JToggleButton>()
    private val colorButtons = mutableMapOf<NoteColorEnum, JToggleButton>()
    private val filterStateManager = SearchFilterStateManager.getInstance()

    fun build(): JPanel {
        filterStateManager.addListener(this)

        val panel =
            JPanel().apply {
                layout = BoxLayout(this, Y_AXIS)
                border = JBUI.Borders.empty(0, 5, 5, 5)
            }

        panel.add(buildDateFilterRow(filterStateManager))
        panel.add(Box.createRigidArea(Dimension(0, JBUI.scale(10))))
        panel.add(buildColorFilterRow(filterStateManager))

        return panel
    }

    private fun buildDateFilterRow(filterStateManager: SearchFilterStateManager): JPanel {
        val rowPanel = JPanel().apply { layout = BoxLayout(this, Y_AXIS) }
        val layout = FlowLayout(LEFT, JBUI.scale(4), 0)
        val filtersPanel =
            JPanel(layout).apply {
                DateFilterEnum.entries.forEach { dateFilter ->
                    val chip =
                        ChipButtonComponent()
                            .build(getMessage(dateFilter.displayNameKey)) { _ ->
                                filterStateManager.toggleDateFilter(dateFilter)
                            }

                    dateButtons[dateFilter] = chip
                    add(chip)
                }
            }

        rowPanel.add(filtersPanel)

        return rowPanel
    }

    private fun buildColorFilterRow(filterStateManager: SearchFilterStateManager): JPanel {
        val rowPanel = JPanel().apply { layout = BoxLayout(this, Y_AXIS) }

        val labelPanel =
            JPanel(FlowLayout(LEFT)).apply {
                add(buildSectionLabel(getMessage("filter.section.color")))
            }
        rowPanel.add(labelPanel)

        val filtersPanel =
            JPanel(FlowLayout(LEFT, JBUI.scale(4), 0)).apply {
                NoteColorEnum.entries
                    .filter { it != NONE }
                    .forEach { colorEnum ->
                        val chip =
                            ChipButtonComponent().buildColorChip(
                                colorEnum.color,
                                getMessage(colorEnum.displayNameKey),
                            ) { _ ->
                                filterStateManager.toggleColorFilter(colorEnum)
                            }
                        colorButtons[colorEnum] = chip
                        add(chip)
                    }
            }
        rowPanel.add(filtersPanel)

        return rowPanel
    }

    private fun buildSectionLabel(text: String): JLabel =
        JLabel(text).apply {
            border = JBUI.Borders.emptyRight(5)
            font = font.deriveFont(font.size - 2f)
        }

    override fun onFilterStateChanged() {
        val filterStateManager = SearchFilterStateManager.getInstance()
        val activeDateFilter = filterStateManager.getActiveDateFilter()

        dateButtons.forEach { (dateFilter, button) ->
            button.isSelected = dateFilter == activeDateFilter
        }

        val activeColors = filterStateManager.getActiveColorFilters()
        colorButtons.forEach { (color, button) ->
            button.isSelected = activeColors.contains(color)
        }
    }
}
