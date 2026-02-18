package com.nazarethlabs.codex

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.nazarethlabs.codex.service.sentry.SentryService

class AppStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        thisLogger().info("MyProjectActivity executed for project: ${project.name}")
        SentryService.getInstance().init()
    }
}
