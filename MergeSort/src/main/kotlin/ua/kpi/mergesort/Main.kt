package ua.kpi.mergesort

import kotlin.math.pow

const val ITEMS_AMOUNT = 1_000_000
const val TESTS_AMOUNT = 10

suspend fun main() {

    test(Algorithm.COROUTINE_OUTER, 0, true)
    test(Algorithm.COROUTINE_OUTER, 1, true)
    test(Algorithm.COROUTINE_OUTER, 2, true)
    test(Algorithm.COROUTINE_OUTER, 3, true)
    test(Algorithm.COROUTINE_OUTER, 0, false)
    test(Algorithm.COROUTINE_OUTER, 1, false)
    test(Algorithm.COROUTINE_OUTER, 2, false)
    test(Algorithm.COROUTINE_OUTER, 3, false)

    test(Algorithm.COROUTINE_INNER, 0, true)
    test(Algorithm.COROUTINE_INNER, 1, true)
    test(Algorithm.COROUTINE_INNER, 2, true)
    test(Algorithm.COROUTINE_INNER, 3, true)
    test(Algorithm.COROUTINE_INNER, 0, false)
    test(Algorithm.COROUTINE_INNER, 1, false)
    test(Algorithm.COROUTINE_INNER, 2, false)
    test(Algorithm.COROUTINE_INNER, 3, false)

    test(Algorithm.THREAD_INNER, 0, true)
    test(Algorithm.THREAD_INNER, 1, true)
    test(Algorithm.THREAD_INNER, 2, true)
    test(Algorithm.THREAD_INNER, 3, true)
    test(Algorithm.THREAD_INNER, 0, false)
    test(Algorithm.THREAD_INNER, 1, false)
    test(Algorithm.THREAD_INNER, 2, false)
    test(Algorithm.THREAD_INNER, 3, false)

    executorService.shutdown()
}

suspend fun test(algorithm: Algorithm, parallelDepth: Int, isRandomArray: Boolean) {
    var resultString = ""
    val array: Array<Int>
    val sort: suspend () -> Unit

    if(isRandomArray) {
        array = descentArray()
        resultString+="In descent array"
    } else {
        array = Array(ITEMS_AMOUNT) {(Math.random() * ITEMS_AMOUNT).toInt()}
        resultString+="In random  array"
    }

    if(parallelDepth == 0) {
        resultString+= " sequent       "
    } else {
        resultString+= " parallel - ${2.toDouble().pow(parallelDepth)}"
    }

    if(algorithm == Algorithm.COROUTINE_INNER){
        resultString+=" coroutines inner"
        sort = suspend { innerSort(array, parallelDepth) }
    } else if(algorithm == Algorithm.COROUTINE_OUTER) {
        resultString+=" coroutines outer"
        sort = suspend { outerSort(array, parallelDepth) }
    } else {
        resultString+=" threads inner   "
        sort = suspend { threadInnerSort(array, parallelDepth) }
    }

    resultString +=  " merge sort on $ITEMS_AMOUNT items"

    var testTime = 0L
    for (i in 0..TESTS_AMOUNT) {
        val begin = System.nanoTime()
        sort()
        val end = System.nanoTime()
        testTime+= end-begin
    }

    resultString += ". Sorting time: ${(testTime/ TESTS_AMOUNT)/1e6} milliseconds, average from $TESTS_AMOUNT tests"

    println(resultString)
}

fun descentArray(): Array<Int> {
    var item = ITEMS_AMOUNT
    val array = Array(ITEMS_AMOUNT) { 0 }
    array.forEachIndexed { index, i ->
        array[index] = item--
    }
    return array;
}

enum class Algorithm {
    COROUTINE_INNER, COROUTINE_OUTER, THREAD_INNER
}


