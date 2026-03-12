package com.nazarethlabs.codex.ui.toolbar.button

import com.intellij.icons.AllIcons.Nodes.Bookmark
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.state.FavoritesFilterStateManager
import com.nazarethlabs.codex.ui.component.ButtonComponent
import javax.swing.JButton

class ToolbarButtonFavoritesFilterComponent {
    fun build(): JButton {
        val favoritesFilterButton =
            ButtonComponent()
                .build(Bookmark, getMessage("toolbar.favorites.filter"))

        favoritesFilterButton.addActionListener {
            FavoritesFilterStateManager.getInstance().toggleFavoritesFilter()
        }

        return favoritesFilterButton
    }
}
