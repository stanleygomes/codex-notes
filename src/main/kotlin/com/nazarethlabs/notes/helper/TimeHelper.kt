package com.nazarethlabs.notes.helper

import com.nazarethlabs.notes.MyBundle

object TimeHelper {
    fun formatTimeAgo(updatedAt: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - updatedAt
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> MyBundle.message("time.now")
            minutes < 60 -> MyBundle.message("time.minutes.ago", minutes)
            hours < 24 -> MyBundle.message("time.hours.ago", hours)
            days < 30 -> MyBundle.message("time.days.ago", days)
            else -> MyBundle.message("time.long.ago")
        }
    }
}
