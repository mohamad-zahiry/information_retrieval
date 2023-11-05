package core.finder

import core.text_process.extractWords
import core.text_process.stemWords
import db.getTempPositionalIndex

fun prepareForPosMatch(
        stemmedWords: List<String>,
        positionalIndex: Map<String, List<Int>>
): List<List<Int>> {

    val positionsList = mutableListOf<List<Int>>()
    for (word in stemmedWords) positionsList.add(positionalIndex[word]!!)
    return positionsList
}

fun positionalMatch(positionsList: List<List<Int>>): Boolean {
    var matched = false

    val listsSearchedIndex = MutableList<Int>(positionsList.size) { 0 }
    var workingList = 0

    var currListIndex: Int

    while (!matched) {
        currListIndex = listsSearchedIndex[workingList]

        val currPos = positionsList[workingList][currListIndex]
        val targetPos = currPos + 1
        val foundIndex = positionsList[workingList + 1].binarySearch(targetPos)

        if (foundIndex < 0) return false

        if (targetPos == positionsList[workingList + 1][foundIndex]) {
            ++workingList
            if (workingList == positionsList.size - 1) matched = true
            listsSearchedIndex[workingList] = foundIndex
        } else {
            workingList = 0
            listsSearchedIndex[0]++
        }
    }

    return matched
}

fun findWithTempPositional(expression: String): List<Int> {
    val stemmedWords = stemWords(extractWords(expression))
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
