package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.DateFilterEnum
import com.nazarethlabs.codex.enum.NoteColorEnum
import java.util.Calendar

class NoteFilterService {
    fun applyFilters(
        notes: List<Note>,
        dateFilter: DateFilterEnum?,
        isFavoriteFilter: Boolean,
        colorFilters: Set<NoteColorEnum>,
    ): List<Note> {
        var filtered = notes

        if (dateFilter != null) {
            filtered = applyDateFilter(filtered, dateFilter)
        }

        if (isFavoriteFilter) {
            filtered = filtered.filter { it.isFavorite }
        }

        if (colorFilters.isNotEmpty()) {
            filtered = filtered.filter { colorFilters.contains(it.color) }
        }

        return filtered
    }

    private fun applyDateFilter(
        notes: List<Note>,
        dateFilter: DateFilterEnum,
    ): List<Note> {
        val startOfPeriod = getStartOfPeriod(dateFilter)
        return notes.filter { it.updatedAt >= startOfPeriod }
    }

    fun getStartOfPeriod(dateFilter: DateFilterEnum): Long = getStartOfPeriodFromCalendar(dateFilter, Calendar.getInstance())

    fun getStartOfPeriodFromCalendar(
        dateFilter: DateFilterEnum,
        calendar: Calendar,
    ): Long {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        when (dateFilter) {
            DateFilterEnum.TODAY -> {}
            DateFilterEnum.THIS_WEEK -> calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            DateFilterEnum.THIS_MONTH -> calendar.set(Calendar.DAY_OF_MONTH, 1)
        }

        return calendar.timeInMillis
    }
}
