package cli

import core.finder.findWithBiword
import core.finder.findWithIntersect
import core.finder.findWithIntersectWithSkips
import core.finder.findWithTempPositional
import core.indexer.createBiwordIndex
import core.indexer.createInvertedIndex
import core.indexer.createPositionalIndexOfDoc
import core.textProcess.InvertedIndex
import db.getDocsIDs
import db.getDocsNamesByIDs
import db.saveDocsIDs
import db.saveInvertedIndex
import db.saveTempPositionalIndex
import settings.DOCS_DIR
import java.io.File
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime

typealias Args = Array<String>

fun cmdAddDocs() {
    val docsNames = File("${DOCS_DIR}/").list()
    if (docsNames!!.size == 0) {
        println("\n${DOCS_DIR} is empty")
        exitProcess(1)
    }

    saveDocsIDs(docsNames, DOCS_DIR)

    println("\n${docsNames.size} documents is found")
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
            println("\n${invertedIndex.size} words is indexed in ${time / 10E9} s")
        }
        "simple" -> {
            val time = measureNanoTime { invertedIndex = createInvertedIndex(docsIDsPaths) }

            saveInvertedIndex(invertedIndex, indexType)
            println("\n${invertedIndex.size} words is indexed in ${time / 10E9} s")
        }
        "positional" -> {
            var time = 0L

            for (docID in docsIDsPaths.keys) {
                time += measureNanoTime { tempPositionalIndex = createPositionalIndexOfDoc(docID) }

                saveTempPositionalIndex(docID, tempPositionalIndex)
                println("${tempPositionalIndex.size} words are indexed of document \"$docID\"")
            }

            println("\n${docsIDsPaths.size} documents are indexed in ${time / 10E9} s")
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
    val foundDocsIDs: List<Int>
    val time: Long

    when (method) {
        "simple" -> {
            time = measureNanoTime { foundDocsIDs = findWithIntersect(expression) }
        }
        "biword" -> {
            time = measureNanoTime { foundDocsIDs = findWithBiword(expression) }
        }
        "temp_positional" -> {
            time = measureNanoTime { foundDocsIDs = findWithTempPositional(expression) }
        }
        else -> {
            cmdHelp()
            exitProcess(1)
        }
    }

    val foundDocs = getDocsNamesByIDs(foundDocsIDs)

    println("\nSearch time: ${time / 10E9} s")
    println("\nSearched text:\n\t\"$expression\"")
    println("\nFound documents (${foundDocs.size}):")
    for (docName in foundDocs) println("\t$docName")
}

fun cmdFindWithSkips(args: Args) {
    val foundDocsIDs: List<Int>
    val expression = args[1]

    val time = measureNanoTime { foundDocsIDs = findWithIntersectWithSkips(expression) }

    val foundDocs = getDocsNamesByIDs(foundDocsIDs)

    println("\nSearch time: ${time / 10E9} s")
    println("\nSearched text:\n\t\"$expression\"")
    println("\nFound documents (${foundDocs.size}):")
    for (docName in foundDocs) println("\t$docName")
}

fun cmdHelp() {
    val helpMsg =
        """add_docs
create_index <type: (biword/simple/positional)>
find <method :(biword/simple/temp_positional)> "<expression>"
find_skip "<expression>"
help"""

    println(helpMsg)
}
