package org.jetbrains.plugins.template.entity

data class Note(
    var id: String = "",
    var title: String = "",
    var filePath: String = "",
    var createdAt: Long = 0L,
    var updatedAt: Long = 0L
)
