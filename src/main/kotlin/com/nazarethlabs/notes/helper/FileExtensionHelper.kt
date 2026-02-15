package com.nazarethlabs.notes.helper

object FileExtensionHelper {
    fun normalizeExtension(extension: String): String {
        var normalized = sanitizeExtension(extension)

        if (normalized.isEmpty()) {
            return ".md"
        }

        if (!normalized.startsWith(".")) {
            normalized = ".$normalized"
        }

        return normalized
    }

    private fun sanitizeExtension(extension: String): String {
        return extension
            .trim()
            .replace(Regex("[^a-zA-Z0-9.]"), "")
    }
}
