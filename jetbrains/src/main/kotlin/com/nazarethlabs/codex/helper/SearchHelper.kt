package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note

object SearchHelper {
    private const val SCORE_TITLE_EXACT_MATCH = 200
    private const val SCORE_TITLE_CONTAINS_QUERY = 100
    private const val SCORE_TITLE_STARTS_WITH_QUERY = 50
    private const val SCORE_TITLE_TERM_CONTAINS = 10
    private const val SCORE_TITLE_TERM_STARTS_WITH = 5
    private const val SCORE_TITLE_WORD_EXACT_MATCH = 7
    private const val SCORE_TITLE_WORD_STARTS_WITH = 3
    private const val SCORE_CONTENT_CONTAINS_QUERY = 30
    private const val SCORE_CONTENT_TERM_MATCH = 5

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
            score += SCORE_CONTENT_CONTAINS_QUERY
        }

        terms.forEach { term ->
            if (contentLower.contains(term)) {
                score += SCORE_CONTENT_TERM_MATCH
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
            score += SCORE_TITLE_CONTAINS_QUERY
        }
        if (title == fullQuery) {
            score += SCORE_TITLE_EXACT_MATCH
        }
        if (title.startsWith(fullQuery)) {
            score += SCORE_TITLE_STARTS_WITH_QUERY
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
                score += SCORE_TITLE_TERM_CONTAINS
            }
            if (title.startsWith(term)) {
                score += SCORE_TITLE_TERM_STARTS_WITH
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
                    score += SCORE_TITLE_WORD_STARTS_WITH
                }
                if (word == term) {
                    score += SCORE_TITLE_WORD_EXACT_MATCH
                }
            }
        }
        return score
    }
}
