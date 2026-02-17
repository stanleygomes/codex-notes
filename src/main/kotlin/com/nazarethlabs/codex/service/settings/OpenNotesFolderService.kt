package com.nazarethlabs.codex.service.settings

import java.awt.Desktop
import java.io.File

class OpenNotesFolderService {
    fun openFolder(path: String): Boolean {
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        return try {
            when {
                Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN) -> {
                    Desktop.getDesktop().open(folder)
                    true
                }
                isLinux() -> {
                    Runtime.getRuntime().exec(arrayOf("xdg-open", folder.absolutePath))
                    true
                }
                isMac() -> {
                    Runtime.getRuntime().exec(arrayOf("open", folder.absolutePath))
                    true
                }
                isWindows() -> {
                    Runtime.getRuntime().exec(arrayOf("explorer", folder.absolutePath))
                    true
                }
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun isLinux(): Boolean = System.getProperty("os.name").lowercase().contains("linux")

    private fun isMac(): Boolean = System.getProperty("os.name").lowercase().contains("mac")

    private fun isWindows(): Boolean = System.getProperty("os.name").lowercase().contains("windows")
}
