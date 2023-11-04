package core.indexer

import core.text_process.InvertedIndex
import core.text_process.extractWords
import core.text_process.stem
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
            var li = positionalIndex[word]!!.lastIndex
            li = positionalIndex[word]!![li]
            positionalIndex[word]!!.add(i - li)
        }
    }
    return positionalIndex
}

fun createPositionalIndex(docsIDsPaths: Map<Int, String>): InvertedIndex {
    val invertedIndex = mutableMapOf<String, MutableList<Int>>()
    var text: String
    var stemmed: String

    // get each file name and index
    for ((id: Int, path: String) in docsIDsPaths.asIterable()) {

        println("start indexing file ($id): \"$path\"")

        // read file data
        text = File(path).readText()

        // update inverted-index with new red file
        for (word in extractWords(text)) {
            stemmed = stem(word)

            try {
                if (!invertedIndex[stemmed]!!.contains(id)) invertedIndex[stemmed]!!.add(id)
            } catch (_: NullPointerException) {
                invertedIndex[stemmed] = mutableListOf()
            }

            if (!invertedIndex[stemmed]!!.contains(id)) invertedIndex[stemmed]!!.add(id)
        }
    }

    return invertedIndex.toSortedMap()
}
