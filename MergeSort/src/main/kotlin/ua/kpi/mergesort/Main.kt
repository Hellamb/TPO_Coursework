package ua.kpi.mergesort

const val ITEMS_AMOUNT = 1_000_000_0
const val TESTS_AMOUNT = 100

suspend fun main() {

    val parallelDepth = 3
    val unsorted = Array(ITEMS_AMOUNT) { (Math.random() * ITEMS_AMOUNT).toInt() }
    val syncResults = Array(TESTS_AMOUNT) { 0L }
    val parallelResults = Array(TESTS_AMOUNT) { 0L }

    syncResults.forEachIndexed { index, i ->
        val begin = System.nanoTime()
        sort(unsorted, 0)
        val end = System.nanoTime()
        syncResults[index] = end - begin
//        println("           Not parallel merge sort on $itemsAmount elements passed in ${end - begin} nano sec")
    }

    parallelResults.forEachIndexed { index, i ->
        val begin = System.nanoTime()
        sort(unsorted, parallelDepth)
        val end = System.nanoTime()
        parallelResults[index] = end - begin
//        println("Parallel with depth: $parallelDepth merge sort on $itemsAmount elements passed in ${end - begin} nano sec")
    }

    println("Sync average: ${syncResults.average()}")
    println("Async average: ${parallelResults.average()}")
}


