package com.nazarethlabs.codex.helper

import java.text.SimpleDateFormat
import java.util.Date

object DateHelper {
    val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

    fun formatDate(date: Date): String = dateTimeFormat.format(date)
}
