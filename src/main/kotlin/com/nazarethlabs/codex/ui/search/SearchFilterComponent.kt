package com.nazarethlabs.codex.ui.search

import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.enum.DateFilterEnum
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.listener.SearchFilterStateListener
import com.nazarethlabs.codex.state.SearchFilterStateManager
import com.nazarethlabs.codex.ui.component.ChipButtonComponent
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JToggleButton

class SearchFilterComponent : SearchFilterStateListener {
    private val dateButtons = mutableMapOf<DateFilterEnum, JToggleButton>()
    private lateinit var favoriteButton: JToggleButton
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
        panel.add(buildFavoriteFilterRow(filterStateManager))
        panel.add(buildColorFilterRow(filterStateManager))

        return panel
    }

    fun dispose() {
        filterStateManager.removeListener(this)
    }

    private fun buildDateFilterRow(filterStateManager: SearchFilterStateManager): JPanel =
        JPanel(FlowLayout(FlowLayout.LEFT, JBUI.scale(4), 0)).apply {
            add(buildSectionLabel(getMessage("filter.section.date")))
            DateFilterEnum.entries.forEach { dateFilter ->
                val chip =
                    ChipButtonComponent().build(getMessage(dateFilter.displayNameKey)) { _ ->
                        filterStateManager.toggleDateFilter(dateFilter)
                    }
                dateButtons[dateFilter] = chip
                add(chip)
            }
        }

    private fun buildFavoriteFilterRow(filterStateManager: SearchFilterStateManager): JPanel =
        JPanel(FlowLayout(FlowLayout.LEFT, JBUI.scale(4), 0)).apply {
            add(buildSectionLabel(getMessage("filter.section.favorite")))
            favoriteButton =
                ChipButtonComponent().build(getMessage("filter.favorite")) { _ ->
                    filterStateManager.toggleFavoriteFilter()
                }
            add(favoriteButton)
        }

    private fun buildColorFilterRow(filterStateManager: SearchFilterStateManager): JPanel =
        JPanel(FlowLayout(FlowLayout.LEFT, JBUI.scale(4), 0)).apply {
            add(buildSectionLabel(getMessage("filter.section.color")))
            NoteColorEnum.entries
                .filter { it != NoteColorEnum.NONE }
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

    private fun buildSectionLabel(text: String): JLabel =
        JLabel(text).apply {
            font = JBUI.Fonts.smallFont()
            border = JBUI.Borders.emptyRight(2)
        }

    override fun onFilterStateChanged() {
        val filterStateManager = SearchFilterStateManager.getInstance()
        val activeDateFilter = filterStateManager.getActiveDateFilter()

        dateButtons.forEach { (dateFilter, button) ->
            button.isSelected = dateFilter == activeDateFilter
        }

        favoriteButton.isSelected = filterStateManager.isFavoriteFilterActive()

        val activeColors = filterStateManager.getActiveColorFilters()
        colorButtons.forEach { (color, button) ->
            button.isSelected = activeColors.contains(color)
        }
    }
}
