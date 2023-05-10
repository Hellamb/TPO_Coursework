package ua.kpi.mergesort

import java.util.concurrent.Executors

val executorService = Executors.newFixedThreadPool(32)

fun threadInnerSort(array: Array<Int>, parallelDepth: Int = 0) {
    if (array.size < 2) return

    val middleIndex = array.size / 2
    val leftArray = array.sliceArray(0 until middleIndex)
    val rightArray = array.sliceArray(middleIndex until array.size)

    if (parallelDepth == 0) {
        threadInnerSort(leftArray)
        threadInnerSort(rightArray)
    } else {

        val leftFuture = executorService.submit {
            threadInnerSort(leftArray, parallelDepth - 1)
        }
        val rightFuture = executorService.submit {
            threadInnerSort(rightArray, parallelDepth - 1)
        }
        leftFuture.get()
        rightFuture.get()
    }

    threadInnerMerge(leftArray, rightArray, array)
}

fun threadInnerMerge(leftArray: Array<Int>, rightArray: Array<Int>, resultArray: Array<Int>) {
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
