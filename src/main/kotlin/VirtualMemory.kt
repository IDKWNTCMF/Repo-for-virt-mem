import java.io.File

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

fun findReplacedFrameOPT(nextAppeal: List<Int>) = nextAppeal.indexOf(nextAppeal.max()) + 1

// this function returns a list of values, in the i-th place is an index of the next appeal to the same `page` if it exists
//                                                           or a unique big Int if it is the last appeal to this `page`
fun getNextAppeal(curClause: Clause): List<Int> {
    val nextAppeal = mutableListOf<Int>()
    val curLastAppeal = MutableList(curClause.processSize + 1) {-1}
    for (index in curClause.appeals.indices) {
        val curAppeal = curClause.appeals[index]
        nextAppeal.add(curClause.appeals.size + curAppeal) // if it is not the last case of this appeal then this value will change later
        if (curLastAppeal[curAppeal] != -1) {
            nextAppeal[curLastAppeal[curAppeal]] = index
        }
        curLastAppeal[curAppeal] = index
    }
    return nextAppeal
}

fun algorithm(process: Process, curClause: Clause): Pair<Int, List<Int>> {
    val RAM = mutableListOf<Int>()
    val nextAppeal = getNextAppeal(curClause)

    val whenEnteredRAM = mutableListOf<Int>()               // this list will be used in FIFO process

    val lastAppealForElementInRAM = mutableListOf<Int>()    // this list will be used in LRU process

    val nextAppealForElementInRAM = mutableListOf<Int>()    // this list will be used in OPT process

    var numberOfReplacements = 0
    val replacements = mutableListOf<Int>()
    for ((curIndex, curAppeal) in curClause.appeals.withIndex()) {
        if (RAM.contains(curAppeal)) {
            replacements.add(0)

            lastAppealForElementInRAM.remove(curAppeal)                                 // this is an `update information` part of
            lastAppealForElementInRAM.add(curAppeal)                                    // LRU process

            nextAppealForElementInRAM[RAM.indexOf(curAppeal)] = nextAppeal[curIndex]    // this is an `update information` part of OPT process
            continue
        }
        numberOfReplacements++
        if (RAM.size < curClause.RAMSize) {
            replacements.add(getNumberOfReplacedFrame(numberOfReplacements, curClause.RAMSize))
            RAM.add(curAppeal)

            whenEnteredRAM.add(curAppeal)                           // this is an `add in RAM` part of FIFO process

            lastAppealForElementInRAM.add(curAppeal)                // this is an `add in RAM` part of LRU process

            nextAppealForElementInRAM.add(nextAppeal[curIndex])     // this is an `add in RAM` part of OPT process
            continue
        }
        val replacedFrame = when (process) {
            Process.FIFO -> findReplacedFrameFIFO(whenEnteredRAM, RAM)
            Process.LRU -> findReplacedFrameLRU(lastAppealForElementInRAM, RAM)
            Process.OPT -> findReplacedFrameOPT(nextAppealForElementInRAM)
        }
        replacements.add(replacedFrame)
        RAM[replacedFrame - 1] = curAppeal

        whenEnteredRAM.remove(whenEnteredRAM.first())                           // this is a `recount` part
        whenEnteredRAM.add(curAppeal)                                           // of FIFO process

        lastAppealForElementInRAM.remove(lastAppealForElementInRAM.first())     // this is a `recount` part
        lastAppealForElementInRAM.add(curAppeal)                                // of LRU process

        nextAppealForElementInRAM[replacedFrame - 1] = nextAppeal[curIndex]     // this is a `recount` part of OPT process
    }
    return Pair(numberOfReplacements, replacements)
}

fun outputTheResultOfAlgorithm(process: Process, resultOfAlgorithm: Pair<Int, List<Int>>, outputFile: String) {
    File(outputFile).appendText("$process: ${resultOfAlgorithm.first} replacements\n")
    resultOfAlgorithm.second.map { File(outputFile).appendText("$it ")}
    File(outputFile).appendText("\n\n")
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