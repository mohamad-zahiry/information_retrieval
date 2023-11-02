package cli

import core.finder.findWithIntersect
import core.finder.findWithIntersectWithSkips
import core.indexer.createBiwordIndex
import core.indexer.createInvertedIndex
import core.text_process.InvertedIndex
import db.getDocsIDs
import db.getDocsNamesByIDs
import db.saveDocsIDs
import db.saveInvertedIndex
import java.io.File
import java.time.LocalDateTime
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime

const val DOCS = "./src/main/resources/books"

typealias Args = Array<String>

fun cmdAddDocs() {
    val docsNames = File("$DOCS/").list()
    if (docsNames != null) saveDocsIDs(docsNames, DOCS)

    println("\n${docsNames?.size} documents is found")
}

fun cmdCreateIndex(args: Args) {
    val indexType = args[1]
    val invertedIndex: InvertedIndex
    val docsIDsPaths = getDocsIDs()
    val time: Long

    when (indexType) {
        "biword" -> {
            println(LocalDateTime.now())
            time = measureNanoTime { invertedIndex = createBiwordIndex(docsIDsPaths) }
            println(LocalDateTime.now())
        }
        "inverted_index" -> {
            println(LocalDateTime.now())
            time = measureNanoTime { invertedIndex = createInvertedIndex(docsIDsPaths) }
            println(LocalDateTime.now())
        }
        else -> exitProcess(1)
    }

    saveInvertedIndex(invertedIndex)

    println("\n${invertedIndex.size} words is indexed in ${time / 10E8} s")
}

fun cmdFind(args: Args) {
    val intersectResult: List<Int>
    val expression = args[1]

    val time = measureNanoTime { intersectResult = findWithIntersect(expression) }

    val foundDocs = getDocsNamesByIDs(intersectResult)

    println("\nSearch time: ${time / 10E9} s")
    println("\nSearched text:\n\t\"$expression\"")
    println("\nFound documents (${foundDocs.size}):")
    for (docName in foundDocs) println("\t$docName")
}

fun cmdFindWithSkips(args: Args) {
    val intersectResult: List<Int>
    val expression = args[1]

    val time = measureNanoTime { intersectResult = findWithIntersectWithSkips(expression) }

    val foundDocs = getDocsNamesByIDs(intersectResult)

    println("\nSearch time: ${time / 10E9} s")
    println("\nSearched text:\n\t\"$expression\"")
    println("\nFound documents (${foundDocs.size}):")
    for (docName in foundDocs) println("\t$docName")
}

fun cmdHelp() {
    val helpMsg =
            """add_docs
create_index <type: (biword/inverted_index)>
find "<expression>"
find_skip "<expression>"
help"""

    println(helpMsg)
}
