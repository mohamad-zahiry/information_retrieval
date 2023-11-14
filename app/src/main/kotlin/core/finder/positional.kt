package core.finder

import core.textProcess.stemWords
import core.textProcess.tokenize
import db.getTempPositionalIndex

fun prepareForPosMatch(
    stemmedWords: List<String>,
    positionalIndex: Map<String, List<Int>>,
): List<List<Int>> {
    val positionsList = mutableListOf<List<Int>>()
    for (word in stemmedWords) positionsList.add(positionalIndex[word]!!)
    return positionsList
}

fun positionalMatch(positionsList: List<List<Int>>): Boolean {
    val listsCurrIndex = MutableList<Int>(positionsList.size) { 0 }
    val lenList1 = positionsList[0].size
    var targetPos: Int
    var foundIndex: Int

    for (l1i in positionsList[0].indices) {
        targetPos = positionsList[0][l1i] + 1

        for (i in 1..<positionsList.size) {
            foundIndex = positionsList[i].binarySearch(targetPos, fromIndex = listsCurrIndex[i])

            if (foundIndex < 0) {
                if (l1i == lenList1 - 1) {
                    return false
                }
                break
            }

            if (i == positionsList.size - 1) {
                return true
            }

            listsCurrIndex[i] = foundIndex
            // increase targetPos to find it in next list
            ++targetPos
        }
    }

    return false
}

fun findWithTempPositional(expression: String): List<Int> {
    val stemmedWords = stemWords(tokenize(expression))
    val intersectResult = findWithIntersect(expression)
    val foundDocsIDs = mutableListOf<Int>()

    var tempPositionalIndex: Map<String, List<Int>>
    var positionsList: List<List<Int>>
    var match: Boolean

    for (docID in intersectResult) {
        tempPositionalIndex = getTempPositionalIndex(docID)
        positionsList = prepareForPosMatch(stemmedWords, tempPositionalIndex)
        match = positionalMatch(positionsList)

        if (match) foundDocsIDs.add(docID)
    }

    return foundDocsIDs
}
