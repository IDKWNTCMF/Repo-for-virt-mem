import java.io.File

data class Replacement(val frameNumber: Int, val replacedValue: Int, val newValue: Int)

data class Clause(val m: Int, val n: Int, val appeals: List<Int>)

fun parseForMN(stringWithMN: String): Pair<Int, Int> {
    val m = stringWithMN.substringBefore(' ').toInt()
    val n = stringWithMN.substringAfter(' ').toInt()
    return Pair(m, n)
}

fun parseForAppeals(appealsInOneString: String): List<Int> {
    var unparsedAppeals = appealsInOneString
    val appeals = mutableListOf<Int>()
    while (' ' in unparsedAppeals) {
        appeals.add(unparsedAppeals.substringBefore(' ').toInt())
        unparsedAppeals = unparsedAppeals.substringAfter(' ')
    }
    if (unparsedAppeals.isNotEmpty()) appeals.add(unparsedAppeals.toInt()) // Add last appeal if it existss
    return appeals
}

fun parseForClause(unparsedMN: String, unparsedAppeals: String): Clause {
    val (m, n) = parseForMN(unparsedMN)
    val appeals = parseForAppeals(unparsedAppeals)
    return Clause(m, n, appeals)
}

fun parseTheInput(input: List<String>): List<Clause> {
    val clauses = mutableListOf<Clause>()
    for (index in input.indices step 2) {
        clauses.add(parseForClause(input[index], input[index + 1]))
    }
    return clauses
}

fun getNumberOfReplacedFrame(curReplacement: Int, m: Int) = if (curReplacement % m == 0) m else curReplacement % m

fun findReplacedFrameFIFO(indexOfElementInMemory: List<Int>, m: Int): Int {
    var replacedFrame = 0
    for (curFrame in 0 until m) {
        if (indexOfElementInMemory[curFrame] < indexOfElementInMemory[replacedFrame]) {
            replacedFrame = curFrame
        }
    }
    return replacedFrame + 1
}

fun findReplacedFrameLRU(processedPrefixSize: Int, curClause: Clause, memory: List<Int>): Int {
    val lastAppeal = MutableList(curClause.n + 1) {-1}
    for (index in 0 until processedPrefixSize) {
        lastAppeal[curClause.appeals[index]] = index
    }
    var replacedFrame = 0
    for (curFrame in 0 until curClause.m) {
        if (lastAppeal[memory[curFrame]] < lastAppeal[memory[replacedFrame]]) {
            replacedFrame = curFrame
        }
    }
    return replacedFrame + 1
}

fun findReplacedFrameOPT(processedPrefixSize: Int, curClause: Clause, memory: List<Int>): Int {
    val nextAppeal = MutableList(curClause.n + 1) {curClause.appeals.size}
    for (index in processedPrefixSize + 1 until curClause.appeals.size) {
        if (nextAppeal[curClause.appeals[index]] == curClause.appeals.size) {
            nextAppeal[curClause.appeals[index]] = index
        }
    }
    var replacedFrame = 0
    for (curFrame in 0 until curClause.m) {
        if (nextAppeal[memory[curFrame]] > nextAppeal[memory[replacedFrame]]) {
            replacedFrame = curFrame
        }
    }
    return replacedFrame + 1
}

fun algorithm(whatAlgorithm: String, curClause: Clause): Pair<Int, List<Replacement>>{
    val memory = mutableListOf<Int>()
    val indexOfElementInMemory = mutableListOf<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for ((curIndex, curPage) in curClause.appeals.withIndex()) {
        if (memory.contains(curPage)) {
            replacements.add(Replacement(0, curPage, curPage))
            continue
        }
        numberOfReplacements++
        if (memory.size < curClause.m) {
            memory.add(curPage)
            indexOfElementInMemory.add(curIndex)
            replacements.add(Replacement(getNumberOfReplacedFrame(numberOfReplacements, curClause.m), 0, curPage))
            continue
        }
        val replacedFrame = when (whatAlgorithm) {
            "FIFO" -> findReplacedFrameFIFO(indexOfElementInMemory, curClause.m)
            "LRU" -> findReplacedFrameLRU(curIndex, curClause, memory)
            else -> findReplacedFrameOPT(curIndex, curClause, memory) // this branch is for an OPT algorithm
        }
        replacements.add(Replacement(replacedFrame, memory[replacedFrame - 1], curPage))
        memory[replacedFrame - 1] = curPage
        indexOfElementInMemory[replacedFrame - 1] = curIndex
    }
    return Pair(numberOfReplacements, replacements)
}

fun outputTheResultOfAlgorithm(whatAlgorithm: String, resultOfAlgorithm: Pair<Int, List<Replacement>>, outputFile: String) {
    File(outputFile).appendText("$whatAlgorithm: ${resultOfAlgorithm.first} replacements\n\n")
    for (replacement in resultOfAlgorithm.second) {
        if (replacement.frameNumber == 0) {
            File(outputFile).appendText("${replacement.newValue}: The needed page is in memory\n")
            continue
        }
        if (replacement.replacedValue == 0) {
            File(outputFile).appendText("Add ${replacement.newValue} in ${replacement.frameNumber} frame\n")
        } else {
            File(outputFile).appendText("Replace ${replacement.replacedValue} with ${replacement.newValue} in ${replacement.frameNumber} frame\n")
        }
    }
    File(outputFile).appendText("\n")
}

fun main(args: Array<String>) {
    val inputFile = if (args.size == 2) args[0] else "input.txt"
    val outputFile = if (args.size == 2) args[1] else "output.txt"
    File(outputFile).writeText("")
    val input = File(inputFile).readLines()
    val clauses = parseTheInput(input)
    for ((curIndex, curClause) in clauses.withIndex()) {
        File(outputFile).appendText("Clause ${curIndex + 1}:\n\n")
        outputTheResultOfAlgorithm("FIFO", algorithm("FIFO", curClause), outputFile)
        outputTheResultOfAlgorithm("LRU", algorithm("LRU", curClause), outputFile)
        outputTheResultOfAlgorithm("OPT", algorithm("OPT", curClause), outputFile)
        File(outputFile).appendText("\n")
    }
}