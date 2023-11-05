package core.finder

import core.text_process.extractWords
import core.text_process.stemWords
import kotlin.math.sqrt

fun intersect(list1: List<Int>, list2: List<Int>): List<Int> {
    val answer = mutableListOf<Int>()

    var pos1 = 0
    var pos2 = 0

    var docID1: Int
    var docID2: Int

    while (pos1 != list1.size && pos2 != list2.size) {
        docID1 = list1[pos1]
        docID2 = list2[pos2]

        if (docID1 == docID2) {
            answer.add(docID1)
            ++pos1
            ++pos2
        } else if (docID1 < docID2) ++pos1 else ++pos2
    }

    return answer
}

fun findWithIntersect(expression: String): List<Int> {
    var intersectResult: List<Int>

    val invertedIndex = db.getInvertedIndex(type = "simple")
    val founds = mutableListOf<List<Int>>()

    val words = stemWords(extractWords(expression))

    for (word in words) if (invertedIndex.containsKey(word)) founds.add(invertedIndex[word]!!)

    founds.sortBy { it.size }

    intersectResult = founds[0]

    for (i in 1 ..< founds.size) intersectResult = intersect(intersectResult, founds[i])

    return intersectResult
}

fun intersectWithSkips(list1: List<Int>, list2: List<Int>): List<Int> {
    val answer = mutableListOf<Int>()

    val skipLen1 = sqrt(list1.size.toDouble()).toInt()
    val skipLen2 = sqrt(list2.size.toDouble()).toInt()

    val hasSkip1 = { index: Int -> (index % skipLen1 == 0) && ((index + skipLen1) < list1.size) }
    val hasSkip2 = { index: Int -> (index % skipLen2 == 0) && ((index + skipLen2) < list2.size) }

    val skip1 = { index: Int -> index + skipLen1 }
    val skip2 = { index: Int -> index + skipLen2 }

    var pos1 = 0
    var pos2 = 0

    var docID1: Int
    var docID2: Int

    while (pos1 < list1.size && pos2 < list2.size) {
        docID1 = list1[pos1]
        docID2 = list2[pos2]

        if (docID1 == docID2) {
            answer.add(docID1)
            pos1 += 1
            pos2 += 1
        } else if (docID1 < docID2) {
            if (hasSkip1(pos1) && (list1[skip1(pos1)] <= docID2))
                    while (hasSkip1(pos1) && (list1[skip1(pos1)] <= docID2)) pos1 = skip1(pos1)
            else pos1 += 1
        } else {
            if (hasSkip2(pos2) && (list2[skip2(pos2)] <= docID1))
                    while (hasSkip2(pos2) && (list2[skip2(pos2)] <= docID1)) pos2 = skip2(pos2)
            else pos2 += 1
        }
    }

    return answer
}

fun findWithIntersectWithSkips(expression: String): List<Int> {
    var intersectResult: List<Int>

    val invertedIndex = db.getInvertedIndex(type = "simple")
    val founds = mutableListOf<List<Int>>()

    val words = stemWords(extractWords(expression))

    for (word in words) if (invertedIndex.containsKey(word)) founds.add(invertedIndex[word]!!)

    founds.sortBy { it.size }

    intersectResult = founds[0]

    for (i in 1 ..< founds.size) intersectResult = intersectWithSkips(intersectResult, founds[i])

    return intersectResult
}

fun findWithBiword(expression: String): List<Int> {
    var intersectResult: List<Int>
    var biword: String

    val invertedIndex = db.getInvertedIndex(type = "biword")
    val founds = mutableListOf<List<Int>>()

    val words = stemWords(extractWords(expression))

    for (i in 0 ..< words.size - 1) {
        biword = "${words[i]} ${words[i+1]}"

        if (invertedIndex.containsKey(biword)) founds.add(invertedIndex[biword]!!)
    }

    founds.sortBy { it.size }

    intersectResult = founds[0]

    for (i in 1 ..< founds.size) intersectResult = intersectWithSkips(intersectResult, founds[i])

    return intersectResult
}
