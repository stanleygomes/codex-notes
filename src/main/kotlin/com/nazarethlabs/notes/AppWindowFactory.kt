package com.nazarethlabs.notes

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.nazarethlabs.notes.ui.ToolWindowPanel

class AppWindowFactory : ToolWindowFactory {

    init {
        thisLogger().info("MyToolWindowFactory initialized")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(project)
        val content = ContentFactory
            .getInstance()
            .createContent(myToolWindow.getContent(), null, false)

        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(private val project: Project) {
        fun getContent() = _root_ide_package_.com.nazarethlabs.notes.ui.ToolWindowPanel()
            .create(project)
    }
}
