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

fun getNumberOfReplacedFrame(curReplacement: Int, RAMSize: Int) = if (curReplacement % RAMSize == 0) RAMSize else curReplacement % RAMSize

fun findReplacedFrameFIFO(whenEnteredRAM: List<Int>, RAM: List<Int>) = RAM.indexOf(whenEnteredRAM[0]) + 1

fun findReplacedFrameLRU(lastAppeal: List<Int>, RAM: List<Int>) = RAM.indexOf(lastAppeal[0]) + 1

fun findReplacedFrameOPT(processedPrefixSize: Int, curClause: Clause, memory: List<Int>): Int {
    val nextAppeal = MutableList(curClause.processSize + 1) {curClause.appeals.size}
    for (index in processedPrefixSize + 1 until curClause.appeals.size) {
        if (nextAppeal[curClause.appeals[index]] == curClause.appeals.size) {
            nextAppeal[curClause.appeals[index]] = index
        }
    }
    var replacedFrame = 0
    for (curFrame in 0 until curClause.RAMSize) {
        if (nextAppeal[memory[curFrame]] > nextAppeal[memory[replacedFrame]]) {
            replacedFrame = curFrame
        }
    }
    return replacedFrame + 1
}

fun algorithm(process: Process, curClause: Clause): Pair<Int, List<Replacement>>{
    val ram = mutableListOf<Int>()
    val whenEnteredRAM = mutableListOf<Int>()
    val lastAppeal = mutableListOf<Int>()
    val indexOfElementInMemory = mutableListOf<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for ((curIndex, curAppeal) in curClause.appeals.withIndex()) {
        if (ram.contains(curAppeal)) {
            lastAppeal.remove(curAppeal)
            lastAppeal.add(curAppeal)
            replacements.add(Replacement(0, curAppeal, curAppeal))
            continue
        }
        numberOfReplacements++
        if (ram.size < curClause.RAMSize) {
            whenEnteredRAM.add(curAppeal)
            lastAppeal.add(curAppeal)
            ram.add(curAppeal)
            indexOfElementInMemory.add(curIndex)
            replacements.add(Replacement(getNumberOfReplacedFrame(numberOfReplacements, curClause.RAMSize), 0, curAppeal))
            continue
        }
        val replacedFrame = when (process) {
            Process.FIFO -> findReplacedFrameFIFO(whenEnteredRAM, ram)
            Process.LRU -> findReplacedFrameLRU(lastAppeal, ram)
            Process.OPT -> findReplacedFrameOPT(curIndex, curClause, ram)
        }
        lastAppeal.remove(lastAppeal.first())
        whenEnteredRAM.remove(whenEnteredRAM.first())
        replacements.add(Replacement(replacedFrame, ram[replacedFrame - 1], curAppeal))
        ram[replacedFrame - 1] = curAppeal
        indexOfElementInMemory[replacedFrame - 1] = curIndex
        whenEnteredRAM.add(curAppeal)
        lastAppeal.add(curAppeal)
    }
    return Pair(numberOfReplacements, replacements)
}

fun outputTheResultOfAlgorithm(process: Process, resultOfAlgorithm: Pair<Int, List<Replacement>>, outputFile: String) {
    File(outputFile).appendText("$process: ${resultOfAlgorithm.first} replacements\n\n")
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
    File(outputFile).writeText("")  //clear the output file from previous tests
    val input = File(inputFile).readLines()
    val clauses = parseTheInput(input)
    for ((curIndex, curClause) in clauses.withIndex()) {
        File(outputFile).appendText("Clause ${curIndex + 1}:\n\n")
        outputTheResultOfAlgorithm(Process.FIFO, algorithm(Process.FIFO, curClause), outputFile)
        outputTheResultOfAlgorithm(Process.LRU, algorithm(Process.LRU, curClause), outputFile)
        outputTheResultOfAlgorithm(Process.OPT, algorithm(Process.OPT, curClause), outputFile)
    }
}