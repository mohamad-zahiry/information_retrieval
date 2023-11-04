package core.text_process

import Stemmer

val englishStopWords =
        listOf(
                "i",
                "me",
                "my",
                "myself",
                "we",
                "our",
                "ours",
                "ourselves",
                "you",
                "your",
                "yours",
                "yourself",
                "yourselves",
                "he",
                "him",
                "his",
                "himself",
                "she",
                "her",
                "hers",
                "herself",
                "it",
                "its",
                "itself",
                "they",
                "them",
                "their",
                "theirs",
                "themselves",
                "what",
                "which",
                "who",
                "whom",
                "this",
                "that",
                "these",
                "those",
                "am",
                "is",
                "are",
                "was",
                "were",
                "be",
                "been",
                "being",
                "have",
                "has",
                "had",
                "having",
                "do",
                "does",
                "did",
                "doing",
                "a",
                "an",
                "the",
                "and",
                "but",
                "if",
                "or",
                "because",
                "as",
                "until",
                "while",
                "of",
                "at",
                "by",
                "for",
                "with",
                "about",
                "against",
                "between",
                "into",
                "through",
                "during",
                "before",
                "after",
                "above",
                "below",
                "to",
                "from",
                "up",
                "down",
                "in",
                "out",
                "on",
                "off",
                "over",
                "under",
                "again",
                "further",
                "then",
                "once",
                "here",
                "there",
                "when",
                "where",
                "why",
                "how",
                "all",
                "any",
                "both",
                "each",
                "few",
                "more",
                "most",
                "other",
                "some",
                "such",
                "no",
                "nor",
                "not",
                "only",
                "own",
                "same",
                "so",
                "than",
                "too",
                "very",
                "s",
                "t",
                "can",
                "will",
                "just",
                "don",
                "should",
                "now"
        )

val stemmer = Stemmer()

fun stem(word: String): String {
    stemmer.add(word.toCharArray(), word.length)
    stemmer.stem()
    return stemmer.toString()
}

fun removeEnglishStopWords(words: List<String>): List<String> {
    return words.filter { it !in englishStopWords }.toList()
}

fun tokenize(text: String): List<String> {
    // extract words of the text without removing duplicates
    val wordRE = Regex("(\\w+)\'\\w+|(\\w+)|\"(\\w+?)\"")
    val wordsList = mutableListOf<String>()
    var word: String

    for (value in wordRE.findAll(text)) {
        word = (value.groups[3] ?: value.groups[2] ?: value.groups[1] ?: value.groups[0])!!.value
        word = word.lowercase()

        wordsList.add(word)
    }

    return wordsList
}

fun extractWords(text: String): List<String> {
    val wordRE = Regex("(\\w+)\'\\w+|(\\w+)|\"(\\w+?)\"")
    val wordsList = mutableListOf<String>()
    var word: String

    for (value in wordRE.findAll(text)) {
        word = (value.groups[3] ?: value.groups[2] ?: value.groups[1] ?: value.groups[0])!!.value
        word = word.lowercase()

        if (word !in wordsList) wordsList.add(word)
    }

    return wordsList
}

fun stemWords(words: Iterable<String>): List<String> {
    val wordsSet = mutableListOf<String>()

    for (word in words) wordsSet.add(stem(word))

    return wordsSet
}
