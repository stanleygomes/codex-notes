package com.nazarethlabs.notes.helper

import com.nazarethlabs.notes.dto.Note

object SearchHelper {
    fun search(
        notes: List<Note>,
        query: String,
    ): List<Note> {
        if (query.isBlank()) {
            return notes
        }

        val searchQuery = query.trim().lowercase()
        val searchTerms = searchQuery.split(" ").filter { it.isNotEmpty() }

        return notes
            .map { note ->
                val score = calculateScore(note, searchQuery, searchTerms)
                note to score
            }.filter { it.second > 0 }
            .sortedByDescending { it.second }
            .map { it.first }
    }

    private fun calculateScore(
        note: Note,
        fullQuery: String,
        terms: List<String>,
    ): Int {
        val title = note.title.lowercase()
        val titleWords = title.split(" ", "-", "_", ".").filter { it.isNotEmpty() }

        return scoreForFullQueryMatch(title, fullQuery) +
            scoreForTermMatches(title, terms) +
            scoreForWordMatches(titleWords, terms)
    }

    private fun scoreForFullQueryMatch(
        title: String,
        fullQuery: String,
    ): Int {
        var score = 0
        if (title.contains(fullQuery)) {
            score += 100
        }
        if (title == fullQuery) {
            score += 200
        }
        if (title.startsWith(fullQuery)) {
            score += 50
        }
        return score
    }

    private fun scoreForTermMatches(
        title: String,
        terms: List<String>,
    ): Int {
        var score = 0
        terms.forEach { term ->
            if (title.contains(term)) {
                score += 10
            }
            if (title.startsWith(term)) {
                score += 5
            }
        }
        return score
    }

    private fun scoreForWordMatches(
        titleWords: List<String>,
        terms: List<String>,
    ): Int {
        var score = 0
        terms.forEach { term ->
            titleWords.forEach { word ->
                if (word.startsWith(term)) {
                    score += 3
                }
                if (word == term) {
                    score += 7
                }
            }
        }
        return score
    }
}
