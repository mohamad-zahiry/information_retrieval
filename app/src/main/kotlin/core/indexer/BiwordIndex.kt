package core.indexer

import core.text_process.InvertedIndex
import core.text_process.extractWords
import core.text_process.stem
import java.io.File

fun createBiwordIndex(docsIDsPaths: Map<Int, String>): InvertedIndex {
    val invertedIndex = mutableMapOf<String, MutableList<Int>>()
    var extractedWords: List<String>
    var stemmed1: String
    var stemmed2: String
    var biword: String

    // get each file name and index
    for ((id: Int, path: String) in docsIDsPaths.asIterable()) {

        println("start indexing file ($id): \"$path\"")

        // read file data and extract its words
        extractedWords = extractWords(File(path).readText())

        // update inverted-index with new red file
        for (i in 0 ..< extractedWords.size - 1) {
            stemmed1 = stem(extractedWords[i])
            stemmed2 = stem(extractedWords[i + 1])
            biword = "$stemmed1 $stemmed2"

            if (!invertedIndex.containsKey(biword)) invertedIndex[biword] = mutableListOf()

            if (!invertedIndex[biword]!!.contains(id)) invertedIndex[biword]!!.add(id)
        }
    }
    return invertedIndex.toSortedMap()
}
