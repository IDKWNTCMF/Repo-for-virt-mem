data class Replacement(val frameNumber: Int, val replacedValue: Int, val newValue: Int)

val m = 3
val n = 5

fun getNumberOfReplacedFrame(curReplacement: Int) = if (curReplacement % m == 0) m else curReplacement % m

fun findReplacedFrameFIFO(indexOfElementInMemory: List<Int>): Int {
    var replacedFrame = 0
    for (curFrame in 0 until m) {
        if (indexOfElementInMemory[curFrame] < indexOfElementInMemory[replacedFrame]) {
            replacedFrame = curFrame
        }
    }
    return replacedFrame + 1
}

fun findReplacedFrameLRU(processedPrefixSize: Int, appeals: List<Int>, memory: List<Int>): Int {
    val lastAppeal = MutableList(n + 1) {-1}
    for (index in 0 until processedPrefixSize) {
        lastAppeal[appeals[index]] = index
    }
    var replacedFrame = 0
    for (curFrame in 0 until m) {
        if (lastAppeal[memory[curFrame]] < lastAppeal[memory[replacedFrame]]) {
            replacedFrame = curFrame
        }
    }
    return replacedFrame + 1
}

fun findReplacedFrameOPT(processedPrefixSize: Int, appeals: List<Int>, memory: List<Int>): Int {
    val nextAppeal = MutableList(n + 1) {appeals.size}
    for (index in processedPrefixSize + 1 until appeals.size) {
        if (nextAppeal[appeals[index]] == appeals.size) {
            nextAppeal[appeals[index]] = index
        }
    }
    var replacedFrame = 0
    for (curFrame in 0 until m) {
        if (nextAppeal[memory[curFrame]] > nextAppeal[memory[replacedFrame]]) {
            replacedFrame = curFrame
        }
    }
    return replacedFrame + 1
}

fun algorithm(whatAlgorithm: String, appeals: List<Int>): Pair<Int, List<Replacement>>{
    val memory = mutableListOf<Int>()
    val indexOfElementInMemory = mutableListOf<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for ((curIndex, curPage) in appeals.withIndex()) {
        if (memory.contains(curPage)) {
            replacements.add(Replacement(0, curPage, curPage))
            continue
        }
        numberOfReplacements++
        if (memory.size < m) {
            memory.add(curPage)
            indexOfElementInMemory.add(curIndex)
            replacements.add(Replacement(getNumberOfReplacedFrame(numberOfReplacements), 0, curPage))
            continue
        }
        val replacedFrame = when (whatAlgorithm) {
            "FIFO" -> findReplacedFrameFIFO(indexOfElementInMemory)
            "LRU" -> findReplacedFrameLRU(curIndex, appeals, memory)
            else -> findReplacedFrameOPT(curIndex, appeals, memory) // this branch is for an OPT algorithm
        }
        replacements.add(Replacement(replacedFrame, memory[replacedFrame - 1], curPage))
        memory[replacedFrame - 1] = curPage
        indexOfElementInMemory[replacedFrame - 1] = curIndex
    }
    return Pair(numberOfReplacements, replacements)
}

fun outputTheResult(whatAlgorithm: String, resultOfAlgorithm: Pair<Int, List<Replacement>>) {
    println("$whatAlgorithm: ${resultOfAlgorithm.first} replacements")
    for (replacement in resultOfAlgorithm.second) {
        if (replacement.frameNumber == 0) {
            println("${replacement.newValue}: The needed page is in memory")
            continue
        }
        if (replacement.replacedValue == 0) {
            println("Add ${replacement.newValue} in ${replacement.frameNumber} frame")
        } else {
            println("Replace ${replacement.replacedValue} with ${replacement.newValue} in ${replacement.frameNumber} frame")
        }
    }
    println("<------The end of $whatAlgorithm output------>")
}

fun main(args: Array<String>) {
    val appeals = listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1)
    outputTheResult("FIFO", algorithm("FIFO", appeals))
    outputTheResult("LRU", algorithm("LRU", appeals))
    outputTheResult("OPT", algorithm("OPT", appeals))
}