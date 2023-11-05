package core.indexer

import core.text_process.stemWords
import core.text_process.tokenize
import db.getDocsNamesByIDs
import java.io.File

fun createPositionalIndexOfDoc(docID: Int): Map<String, List<Int>> {
    val positionalIndex = mutableMapOf<String, MutableList<Int>>()

    // open document file
    val file = File(getDocsNamesByIDs(listOf(docID)).get(0))
    val text = file.readText()

    // tokenize the document words
    val tokens = stemWords(tokenize(text))

    var word: String

    for (i in tokens.indices) {
        word = tokens[i]
        if (!positionalIndex.containsKey(word)) positionalIndex[word] = mutableListOf<Int>(i)
        else {
            var lastIndex = positionalIndex[word]!!.lastIndex
            lastIndex = positionalIndex[word]!![lastIndex]
            // saving the difference of last index and current takes fewer space in disk
            positionalIndex[word]!!.add(i - lastIndex)
        }
    }
    return positionalIndex
}
