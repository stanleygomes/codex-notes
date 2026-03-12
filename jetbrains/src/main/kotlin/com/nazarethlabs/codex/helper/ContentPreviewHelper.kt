package com.nazarethlabs.codex.helper

object ContentPreviewHelper {
    private const val MAX_PREVIEW_LENGTH = 30

    fun generatePreview(
        content: String?,
        maxLength: Int = MAX_PREVIEW_LENGTH,
    ): String {
        if (content.isNullOrBlank()) return ""
        val cleaned = content.replace("\n", " ").trim()
        if (cleaned.length <= maxLength) return cleaned

        return cleaned.substring(0, maxLength) + "..."
    }
}
