package db

import core.text_process.InvertedIndex
import utils.empty
import java.io.File

const val DB_PATH = "./src/main/db"
const val DOCS_IDS_PATH = "$DB_PATH/files_index"
const val INVERTED_INDEX_PATH = "$DB_PATH/inverted_index"


fun saveDocsIDs(docsNames: Array<String>, src: String) {
    val docsIDsFile = File(DOCS_IDS_PATH).empty()
    for ((i, name) in docsNames.withIndex())
        if (File("$src/$name").isFile)
            docsIDsFile.appendText("$i $src/$name\n")
}

fun getDocsIDs(): Map<Int, String> {
    val docsIDs = mutableMapOf<Int, String>()
    for (line in File(DOCS_IDS_PATH).readLines()) {
        val (id, name) = line.split(" ", limit = 2)
        docsIDs[id.toInt()] = name
    }
    return docsIDs
}


fun saveInvertedIndex(invertedIndex: InvertedIndex) {
    val invertedIndexFile = File(INVERTED_INDEX_PATH).empty()
    for ((word, index) in invertedIndex.asIterable())
        invertedIndexFile.appendText("$word\n${index.joinToString(" ")}\n")
}


fun getInvertedIndex(): InvertedIndex {
    val invertedIndex = mutableMapOf<String, List<Int>>()
    val invertedIndexFile = File(INVERTED_INDEX_PATH).readLines()


    for (i in invertedIndexFile.indices step 2) {
        val postingsList = mutableListOf<Int>()

        val word = invertedIndexFile[i]

        for (id in invertedIndexFile[i + 1].split(" "))
            postingsList.add(id.toInt())

        invertedIndex[word] = postingsList

    }
    return invertedIndex.toSortedMap()
}


fun getDocsNamesByIDs(ids: List<Int>): List<String> {
    val docs = getDocsIDs().filterKeys { it in ids }
    return docs.values.toList()
}
