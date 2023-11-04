package cli

import core.finder.findWithBiword
import core.finder.findWithIntersect
import core.finder.findWithIntersectWithSkips
import core.indexer.createBiwordIndex
import core.indexer.createInvertedIndex
import core.indexer.createPositionalIndexOfDoc
import core.text_process.InvertedIndex
import db.getDocsIDs
import db.getDocsNamesByIDs
import db.saveDocsIDs
import db.saveInvertedIndex
import db.saveTempPositionalIndex
import java.io.File
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime
import settings.DOCS_DIR

typealias Args = Array<String>

fun cmdAddDocs() {
    val docsNames = File("${DOCS_DIR}/").list()
    if (docsNames != null) saveDocsIDs(docsNames, DOCS_DIR)

    println("\n${docsNames?.size} documents is found")
}

fun cmdCreateIndex(args: Args) {
    val indexType = args[1]
    val invertedIndex: InvertedIndex
    var tempPositionalIndex: Map<String, List<Int>>
    val docsIDsPaths = getDocsIDs()

    when (indexType) {
        "biword" -> {
            val time = measureNanoTime { invertedIndex = createBiwordIndex(docsIDsPaths) }

            saveInvertedIndex(invertedIndex, indexType)
            println("\n${invertedIndex.size} words is indexed in ${time / 10E8} s")
        }
        "simple" -> {
            val time = measureNanoTime { invertedIndex = createInvertedIndex(docsIDsPaths) }

            saveInvertedIndex(invertedIndex, indexType)
            println("\n${invertedIndex.size} words is indexed in ${time / 10E8} s")
        }
        "positional" -> {
            var time = 0L

            for (docID in docsIDsPaths.keys) {
                time += measureNanoTime { tempPositionalIndex = createPositionalIndexOfDoc(docID) }

                saveTempPositionalIndex(docID, tempPositionalIndex)
                println("${tempPositionalIndex.size} words are indexed of document \"$docID\"")
            }

            println("\n${docsIDsPaths.size} documents are indexed in ${time / 10E8} s")
        }
        else -> {
            cmdHelp()
            exitProcess(1)
        }
    }
}

fun cmdFind(args: Args) {
    val method = args[1]
    val expression = args[2]
    val intersectResult: List<Int>
    val time: Long

    when (method) {
        "simple" -> {
            time = measureNanoTime { intersectResult = findWithIntersect(expression) }
        }
        "biword" -> {
            time = measureNanoTime { intersectResult = findWithBiword(expression) }
        }
        else -> {
            cmdHelp()
            exitProcess(1)
        }
    }

    val foundDocs = getDocsNamesByIDs(intersectResult)

    println("\nSearch time: ${time / 10E8} s")
    println("\nSearched text:\n\t\"$expression\"")
    println("\nFound documents (${foundDocs.size}):")
    for (docName in foundDocs) println("\t$docName")
}

fun cmdFindWithSkips(args: Args) {
    val intersectResult: List<Int>
    val expression = args[1]

    val time = measureNanoTime { intersectResult = findWithIntersectWithSkips(expression) }

    val foundDocs = getDocsNamesByIDs(intersectResult)

    println("\nSearch time: ${time / 10E8} s")
    println("\nSearched text:\n\t\"$expression\"")
    println("\nFound documents (${foundDocs.size}):")
    for (docName in foundDocs) println("\t$docName")
}

fun cmdHelp() {
    val helpMsg =
            """add_docs
create_index <type: (biword/simple)>
find <method :(biword/simple)> "<expression>"
find_skip "<expression>"
help"""

    println(helpMsg)
}
