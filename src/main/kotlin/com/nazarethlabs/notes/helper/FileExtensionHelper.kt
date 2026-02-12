package com.nazarethlabs.notes.helper

object FileExtensionHelper {

    fun normalizeExtension(extension: String): String {
        var normalized = extension.trim()
            .replace(Regex("[^a-zA-Z0-9.]"), "")

        if (normalized.isEmpty()) {
            return ".md"
        }

        if (!normalized.startsWith(".")) {
            normalized = ".$normalized"
        }

        return normalized
    }
}
