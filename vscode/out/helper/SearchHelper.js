"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SearchHelper = void 0;
class SearchHelper {
    static SCORE_TITLE_EXACT_MATCH = 200;
    static SCORE_TITLE_CONTAINS_QUERY = 100;
    static SCORE_TITLE_STARTS_WITH_QUERY = 50;
    static SCORE_TITLE_TERM_CONTAINS = 10;
    static SCORE_TITLE_TERM_STARTS_WITH = 5;
    static SCORE_TITLE_WORD_EXACT_MATCH = 7;
    static SCORE_TITLE_WORD_STARTS_WITH = 3;
    static SCORE_CONTENT_CONTAINS_QUERY = 30;
    static SCORE_CONTENT_TERM_MATCH = 5;
    static search(notes, query, contentMap = new Map()) {
        if (!query.trim()) {
            return notes;
        }
        const searchQuery = query.trim().toLowerCase();
        const searchTerms = searchQuery.split(' ').filter((t) => t.length > 0);
        return notes
            .map((note) => {
            const content = contentMap.get(note.id) ?? '';
            const score = SearchHelper.calculateScore(note, searchQuery, searchTerms, content);
            return { note, score };
        })
            .filter((entry) => entry.score > 0)
            .sort((a, b) => b.score - a.score)
            .map((entry) => entry.note);
    }
    static calculateScore(note, fullQuery, terms, content) {
        const title = note.title.toLowerCase();
        const titleWords = title.split(/[ \-_.]+/).filter((w) => w.length > 0);
        return (SearchHelper.scoreForFullQueryMatch(title, fullQuery) +
            SearchHelper.scoreForTermMatches(title, terms) +
            SearchHelper.scoreForWordMatches(titleWords, terms) +
            SearchHelper.scoreForContentMatches(content, fullQuery, terms));
    }
    static scoreForFullQueryMatch(title, fullQuery) {
        let score = 0;
        if (title.includes(fullQuery)) {
            score += SearchHelper.SCORE_TITLE_CONTAINS_QUERY;
        }
        if (title === fullQuery) {
            score += SearchHelper.SCORE_TITLE_EXACT_MATCH;
        }
        if (title.startsWith(fullQuery)) {
            score += SearchHelper.SCORE_TITLE_STARTS_WITH_QUERY;
        }
        return score;
    }
    static scoreForTermMatches(title, terms) {
        let score = 0;
        for (const term of terms) {
            if (title.includes(term)) {
                score += SearchHelper.SCORE_TITLE_TERM_CONTAINS;
            }
            if (title.startsWith(term)) {
                score += SearchHelper.SCORE_TITLE_TERM_STARTS_WITH;
            }
        }
        return score;
    }
    static scoreForWordMatches(titleWords, terms) {
        let score = 0;
        for (const term of terms) {
            for (const word of titleWords) {
                if (word.startsWith(term)) {
                    score += SearchHelper.SCORE_TITLE_WORD_STARTS_WITH;
                }
                if (word === term) {
                    score += SearchHelper.SCORE_TITLE_WORD_EXACT_MATCH;
                }
            }
        }
        return score;
    }
    static scoreForContentMatches(content, fullQuery, terms) {
        if (!content.trim()) {
            return 0;
        }
        const contentLower = content.toLowerCase();
        let score = 0;
        if (contentLower.includes(fullQuery)) {
            score += SearchHelper.SCORE_CONTENT_CONTAINS_QUERY;
        }
        for (const term of terms) {
            if (contentLower.includes(term)) {
                score += SearchHelper.SCORE_CONTENT_TERM_MATCH;
            }
        }
        return score;
    }
}
exports.SearchHelper = SearchHelper;
//# sourceMappingURL=SearchHelper.js.map