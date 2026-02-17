package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note

object SearchHelper {
    fun search(
        notes: List<Note>,
        query: String,
        contentMap: Map<String, String> = emptyMap(),
    ): List<Note> {
        if (query.isBlank()) {
            return notes
        }

        val searchQuery = query.trim().lowercase()
        val searchTerms = searchQuery.split(" ").filter { it.isNotEmpty() }

        return notes
            .map { note ->
                val content = contentMap[note.id] ?: ""
                val score = calculateScore(note, searchQuery, searchTerms, content)
                note to score
            }.filter { it.second > 0 }
            .sortedByDescending { it.second }
            .map { it.first }
    }

    private fun calculateScore(
        note: Note,
        fullQuery: String,
        terms: List<String>,
        content: String = "",
    ): Int {
        val title = note.title.lowercase()
        val titleWords = title.split(" ", "-", "_", ".").filter { it.isNotEmpty() }

        return scoreForFullQueryMatch(title, fullQuery) +
            scoreForTermMatches(title, terms) +
            scoreForWordMatches(titleWords, terms) +
            scoreForContentMatches(content, fullQuery, terms)
    }

    private fun scoreForContentMatches(
        content: String,
        fullQuery: String,
        terms: List<String>,
    ): Int {
        if (content.isBlank()) {
            return 0
        }

        val contentLower = content.lowercase()
        var score = 0

        if (contentLower.contains(fullQuery)) {
            score += 30
        }

        terms.forEach { term ->
            if (contentLower.contains(term)) {
                score += 5
            }
        }

        return score
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
