import java.io.File

data class Replacement(val frameNumber: Int, val replacedValue: Int, val newValue: Int)

data class Clause(val RAMSize: Int, val processSize: Int, val appeals: List<Int>)

enum class Process {
    FIFO, LRU, OPT
}

fun parseClause(unparsedMN: String, unparsedAppeals: String): Clause {
    val (RAMSize, processSize) = unparsedMN.split(" ").map { it.toInt() }
    val appeals = unparsedAppeals.split(" ").map { it.toInt() }
    return Clause(RAMSize, processSize, appeals)
}

fun parseTheInput(input: List<String>): List<Clause> {
    val clauses = mutableListOf<Clause>()
    for (index in input.indices step 2) {
        clauses.add(parseClause(input[index], input[index + 1]))
    }
    return clauses
}

// this function returns a list of values,
// in the i-th place is an index of the next appeal to the same `page` if it exists
// or a unique big Int if it is the last appeal to this `page`
fun getNextAppeal(curClause: Clause): List<Int> {
    val nextAppeal = mutableListOf<Int>()
    val curLastAppeal = MutableList(curClause.processSize + 1) {-1}
    for (index in curClause.appeals.indices) {
        val curAppeal = curClause.appeals[index]
        nextAppeal.add(curClause.appeals.size + curAppeal)
        // if it is not the last case of this appeal then this value will change later
        if (curLastAppeal[curAppeal] != -1) {
            nextAppeal[curLastAppeal[curAppeal]] = index
        }
        curLastAppeal[curAppeal] = index
    }
    return nextAppeal
}

fun Process.update (RAM: List<Int>, additionalList: MutableList<Int>, appeal: Int, newValueForOPT: Int) {
    when (this) {
        Process.FIFO -> {}
        Process.LRU -> {
            additionalList.remove(appeal)
            additionalList.add(appeal)
        }
        Process.OPT -> additionalList[RAM.indexOf(appeal)] = newValueForOPT
    }
}

fun Process.add (additionalList: MutableList<Int>, newValueForFIFOorLRU: Int, newValueForOPT: Int) {
    when (this) {
        Process.OPT -> additionalList.add(newValueForOPT)
        else -> additionalList.add(newValueForFIFOorLRU)
    }
}

fun Process.findReplacedFrame (RAM: List<Int>, additionalList: MutableList<Int>): Int {
    return when (this) {
        Process.OPT -> additionalList.indexOf(additionalList.max()) + 1
        else -> RAM.indexOf(additionalList[0]) + 1
    }
}

fun Process.recount (additionalList: MutableList<Int>, appeal: Int, replacedFrame: Int, newValueForOPT: Int) {
    when (this) {
        Process.OPT -> additionalList[replacedFrame - 1] = newValueForOPT
        else -> {
            additionalList.remove(additionalList.first())
            additionalList.add(appeal)
        }
    }
}

fun algorithm(process: Process, curClause: Clause): Pair<Int, List<Replacement>> {
    val RAM = mutableListOf<Int>()
    val nextAppeal = getNextAppeal(curClause)
    val additionalList = mutableListOf<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for ((curIndex, curAppeal) in curClause.appeals.withIndex()) {
        if (RAM.contains(curAppeal)) {
            replacements.add(Replacement(0, curAppeal, curAppeal))
            process.update(RAM, additionalList, curAppeal, nextAppeal[curIndex])
            continue
        }
        numberOfReplacements++
        if (RAM.size < curClause.RAMSize) {
            replacements.add(Replacement(RAM.size + 1, 0, curAppeal))
            RAM.add(curAppeal)
            process.add(additionalList, curAppeal, nextAppeal[curIndex])
            continue
        }
        val replacedFrame = process.findReplacedFrame(RAM, additionalList)
        replacements.add(Replacement(replacedFrame, RAM[replacedFrame - 1], curAppeal))
        RAM[replacedFrame - 1] = curAppeal
        process.recount(additionalList, curAppeal, replacedFrame, nextAppeal[curIndex])
    }
    return Pair(numberOfReplacements, replacements)
}

fun outputTheResultOfAlgorithm(process: Process, resultOfAlgorithm: Pair<Int, List<Replacement>>, outputFile: String) {
    File(outputFile).appendText("$process: ${resultOfAlgorithm.first} replacements\n")
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
    val (inputFile, outputFile) = if (args.size == 2) (args[0] to args[1]) else ("input.txt" to "output.txt")
    File(outputFile).writeText("")  // clear the output file from previous tests
    val input = File(inputFile).readLines()
    val clauses = parseTheInput(input)
    for ((curIndex, curClause) in clauses.withIndex()) {
        File(outputFile).appendText("Clause ${curIndex + 1}:\n\n")
        outputTheResultOfAlgorithm(Process.FIFO, algorithm(Process.FIFO, curClause), outputFile)
        outputTheResultOfAlgorithm(Process.LRU, algorithm(Process.LRU, curClause), outputFile)
        outputTheResultOfAlgorithm(Process.OPT, algorithm(Process.OPT, curClause), outputFile)
    }
}