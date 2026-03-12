package com.nazarethlabs.codex

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.nazarethlabs.codex.helper.SentryHelper
import com.nazarethlabs.codex.service.note.NoteMigrationLegacyService

class AppStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        thisLogger().info("MyProjectActivity executed for project: ${project.name}")
        SentryHelper.init()
        NoteMigrationLegacyService().migrateIfNeeded()
    }
}
