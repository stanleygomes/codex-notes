package org.jetbrains.plugins.template.entity

import java.time.ZonedDateTime

data class Note(
    val title: String,
    val content: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
