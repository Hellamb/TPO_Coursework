package ua.kpi.mergesort

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun innerSort(array: Array<Int>, parallelDepth: Int = 0) {
    if (array.size < 2) return

    val middleIndex = array.size / 2
    val leftArray = array.sliceArray(0 until middleIndex)
    val rightArray = array.sliceArray(middleIndex until array.size)

    if (parallelDepth == 0) {
        innerSort(leftArray)
        innerSort(rightArray)
    } else {
        coroutineScope {
            val leftDeferred = launch { innerSort(leftArray, parallelDepth - 1) }
            val rightDeferred = launch { innerSort(rightArray, parallelDepth - 1) }
            leftDeferred.join()
            rightDeferred.join()
        }
    }

    innerMerge(leftArray, rightArray, array)
}

fun innerMerge(leftArray: Array<Int>, rightArray: Array<Int>, resultArray: Array<Int>) {
    var i = 0
    var j = 0
    var k = 0

    while (i < leftArray.size && j < rightArray.size) {
        if (leftArray[i] <= rightArray[j]) {
            resultArray[k] = leftArray[i]
            i++
        } else {
            resultArray[k] = rightArray[j]
            j++
        }
        k++
    }

    while (i < leftArray.size) {
        resultArray[k] = leftArray[i]
        i++
        k++
    }

    while (j < rightArray.size) {
        resultArray[k] = rightArray[j]
        j++
        k++
    }
}
