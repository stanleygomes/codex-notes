package com.nazarethlabs.codex

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.nazarethlabs.codex.helper.SentryHelper

class AppStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        thisLogger().info("MyProjectActivity executed for project: ${project.name}")
        SentryHelper.init()
    }
}
