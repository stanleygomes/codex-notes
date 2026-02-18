package com.nazarethlabs.codex.helper

import java.awt.Desktop

object OpenFolderHelper {
    fun openFolder(path: String): Boolean {
        val folder = FileHelper.ensureDirectoryExists(path)

        return try {
            when {
                Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN) -> {
                    Desktop.getDesktop().open(folder)
                    true
                }
                isLinux() -> {
                    ProcessBuilder("xdg-open", folder.absolutePath).start()
                    true
                }
                isMac() -> {
                    ProcessBuilder("open", folder.absolutePath).start()
                    true
                }
                isWindows() -> {
                    ProcessBuilder("explorer", folder.absolutePath).start()
                    true
                }
                else -> false
            }
        } catch (e: Exception) {
            SentryHelper.captureException(e)
            false
        }
    }

    private fun isLinux(): Boolean = System.getProperty("os.name").lowercase().contains("linux")

    private fun isMac(): Boolean = System.getProperty("os.name").lowercase().contains("mac")

    private fun isWindows(): Boolean = System.getProperty("os.name").lowercase().contains("windows")
}
