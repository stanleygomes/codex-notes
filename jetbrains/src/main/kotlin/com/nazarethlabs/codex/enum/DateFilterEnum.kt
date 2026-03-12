package com.nazarethlabs.codex.enum

enum class DateFilterEnum(
    val displayNameKey: String,
) {
    TODAY("filter.date.today"),
    THIS_WEEK("filter.date.this.week"),
    THIS_MONTH("filter.date.this.month"),
}
