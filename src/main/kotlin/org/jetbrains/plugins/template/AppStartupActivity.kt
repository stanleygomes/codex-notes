package org.jetbrains.plugins.template

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class AppStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        thisLogger().info("MyProjectActivity executed for project: ${project.name}")
    }
}
