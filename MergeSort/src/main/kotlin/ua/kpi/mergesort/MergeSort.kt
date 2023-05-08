package ua.kpi.mergesort

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun sort(unsortedArr: Array<Int>, parallelDepth: Int): Array<Int> {
    if (unsortedArr.size < 2) return unsortedArr.copyOf()

    val mid = unsortedArr.size / 2
    val unsortedLeft = unsortedArr.sliceArray(0 until mid)
    val unsortedRight = unsortedArr.sliceArray(mid until unsortedArr.size)
    val leftDeferred: Deferred<Array<Int>>
    val rightDeferred: Deferred<Array<Int>>
    val left: Array<Int>?
    val right: Array<Int>?

    if (parallelDepth > 0) {
        coroutineScope {
            leftDeferred = async { sort(unsortedLeft, parallelDepth - 1) }
            rightDeferred = async { sort(unsortedRight, parallelDepth - 1) }
        }
        left = leftDeferred.await()
        right = rightDeferred.await()
    } else {
        left = sort(unsortedLeft, 0)
        right = sort(unsortedRight, 0)
    }

    return merge(left, right)
}

fun merge(leftArray: Array<Int>, rightArray: Array<Int>): Array<Int> {
    val sortedArr = Array(leftArray.size + rightArray.size) { 0 }
    var i = 0
    var j = 0
    var k = 0

    while (i < leftArray.size && j < rightArray.size) {
        if (leftArray[i] <= rightArray[j]) {
            sortedArr[k] = leftArray[i]
            i++
        } else {
            sortedArr[k] = rightArray[j]
            j++
        }
        k++
    }

    while (i < leftArray.size) {
        sortedArr[k] = leftArray[i]
        i++
        k++
    }

    while (j < rightArray.size) {
        sortedArr[k] = rightArray[j]
        j++
        k++
    }

    return sortedArr
}
