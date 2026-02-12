package org.jetbrains.plugins.template.ui.toolbar.button

import com.intellij.openapi.project.Project
import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import org.jetbrains.plugins.template.enum.SortTypeEnum
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.repository.NotesSettingsRepository
import org.jetbrains.plugins.template.ui.component.ButtonComponent
import org.jetbrains.plugins.template.ui.noteslist.NotesListComponent
import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class ToolbarButtonSortComponent {

    fun build(project: Project, notesListComponent: NotesListComponent): JButton {
        val sortButton = ButtonComponent()
            .build(
                AllIcons.ObjectBrowser.Sorted,
                MessageHelper.getMessage("toolbar.note.sort")
            )

        sortButton.addActionListener { event ->
            val menu = JPopupMenu()
            menu.border = JBUI.Borders.empty(5)

            menu.add(
                createSortMenuItem(project, "toolbar.sort.by.title", SortTypeEnum.TITLE, notesListComponent)
            )
            menu.add(
                createSortMenuItem(project, "toolbar.sort.by.date", SortTypeEnum.DATE, notesListComponent)
            )
            menu.add(
                createSortMenuItem(project, "toolbar.sort.by.favorite", SortTypeEnum.FAVORITE, notesListComponent)
            )

            val component = event.source as JButton
            menu.show(component, 0, component.height)
        }

        return sortButton
    }

    private fun createSortMenuItem(project: Project, messageKey: String, sortType: SortTypeEnum, notesListComponent: NotesListComponent): JMenuItem {
        val itemInset = JBUI.Borders.empty(5, 10, 5, 10)
        val item = JMenuItem(MessageHelper.getMessage(messageKey))
        item.border = itemInset
        item.addActionListener {
            notesListComponent.sortBy(sortType)
            NotesSettingsRepository.getInstance(project).setDefaultSortType(sortType)
        }
        return item
    }
}
