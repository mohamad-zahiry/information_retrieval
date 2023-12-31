package core.indexer

// import core.textProcess.removeEnglishStopWords
import core.textProcess.InvertedIndex
import core.textProcess.extractWords
import core.textProcess.stem
import java.io.File

fun createInvertedIndex(docsIDsPaths: Map<Int, String>): InvertedIndex {
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
