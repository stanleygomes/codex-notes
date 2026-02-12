package com.nazarethlabs.notes.helper

object TimeHelper {

    fun formatTimeAgo(updatedAt: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - updatedAt
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "agora"
            minutes < 60 -> "${minutes}min atrás"
            hours < 24 -> "${hours}h atrás"
            days < 30 -> "${days}d atrás"
            else -> "há muito tempo"
        }
    }
}
