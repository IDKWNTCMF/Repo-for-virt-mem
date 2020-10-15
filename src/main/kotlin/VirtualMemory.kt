import java.io.File
import java.io.FileNotFoundException
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException

data class Replacement(val frameNumber: Int, val replacedValue: Int, val newValue: Int)

data class Clause(val RAMSize: Int, val maxNumberOfAppeal: Int, val appeals: List<Int>)

enum class Process {
    FIFO, LRU, OPT
}

fun parseClause(unparsedSizes: String, unparsedAppeals: String): Clause {
    val (RAMSize, processSize) = unparsedSizes.split(" ").map { it.toInt() }
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
    val curLastAppeal = MutableList(curClause.maxNumberOfAppeal + 1) {-1}
    for (index in curClause.appeals.indices) {
        val curAppeal = curClause.appeals[index]
        nextAppeal.add(curClause.appeals.size + curAppeal)
        // if it is not the last case of this appeal then this value will be changed later
        if (curLastAppeal[curAppeal] != -1) {
            nextAppeal[curLastAppeal[curAppeal]] = index
        }
        curLastAppeal[curAppeal] = index
    }
    return nextAppeal
}

fun Process.update(RAM: List<Int>, auxiliaryArray: MutableList<Int>, appeal: Int, newValueForOPT: Int) {
    when (this) {
        Process.FIFO -> {}
        Process.LRU -> {
            auxiliaryArray.remove(appeal)
            auxiliaryArray.add(appeal)
        }
        Process.OPT -> auxiliaryArray[RAM.indexOf(appeal)] = newValueForOPT
    }
}

fun Process.add(auxiliaryArray: MutableList<Int>, newValueForFIFOorLRU: Int, newValueForOPT: Int) {
    when (this) {
        Process.OPT -> auxiliaryArray.add(newValueForOPT)
        else -> auxiliaryArray.add(newValueForFIFOorLRU)
    }
}

fun Process.findReplacedFrame(RAM: List<Int>, auxiliaryArray: MutableList<Int>): Int {
    return when (this) {
        Process.OPT -> auxiliaryArray.indexOf(auxiliaryArray.max()) + 1
        else -> RAM.indexOf(auxiliaryArray[0]) + 1
    }
}

fun Process.recount(auxiliaryArray: MutableList<Int>, appeal: Int, replacedFrame: Int, newValueForOPT: Int) {
    when (this) {
        Process.OPT -> auxiliaryArray[replacedFrame - 1] = newValueForOPT
        else -> {
            auxiliaryArray.remove(auxiliaryArray.first())
            auxiliaryArray.add(appeal)
        }
    }
}

fun algorithm(process: Process, curClause: Clause): Pair<Int, List<Replacement>> {
    val RAM = mutableListOf<Int>()
    val nextAppeal = getNextAppeal(curClause)
    val auxiliaryArray = mutableListOf<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for ((curIndex, curAppeal) in curClause.appeals.withIndex()) {
        if (RAM.contains(curAppeal)) {
            replacements.add(Replacement(0, curAppeal, curAppeal))
            process.update(RAM, auxiliaryArray, curAppeal, nextAppeal[curIndex])
            continue
        }
        numberOfReplacements++
        if (RAM.size < curClause.RAMSize) {
            replacements.add(Replacement(RAM.size + 1, 0, curAppeal))
            RAM.add(curAppeal)
            process.add(auxiliaryArray, curAppeal, nextAppeal[curIndex])
            continue
        }
        val replacedFrame = process.findReplacedFrame(RAM, auxiliaryArray)
        replacements.add(Replacement(replacedFrame, RAM[replacedFrame - 1], curAppeal))
        RAM[replacedFrame - 1] = curAppeal
        process.recount(auxiliaryArray, curAppeal, replacedFrame, nextAppeal[curIndex])
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
            File(outputFile).appendText("Replace ${replacement.replacedValue} " +
                    "with ${replacement.newValue} in ${replacement.frameNumber} frame\n")
        }
    }
    File(outputFile).appendText("\n")
}

fun checkClause(curClause: Clause): Int {
    for ((curAppealNumber, appeal) in curClause.appeals.withIndex()) {
        if (appeal > curClause.maxNumberOfAppeal) return curAppealNumber + 1
    }
    return 0
}

fun main(args: Array<String>) {
    try {
        val (inputFile, outputFile) = (args[0] to args[1])
        File(outputFile).writeText("")  // clear the output file from previous tests
        val input = File(inputFile).readLines()
        val clauses = parseTheInput(input)
        for ((numberOfClause, curClause) in clauses.withIndex()) {
            val resultOfCheckClause = checkClause(curClause)
            if (resultOfCheckClause != 0) {
                println("$resultOfCheckClause element in clause ${numberOfClause + 1} is more than expected!")
                continue
            }
            if (curClause.RAMSize >= curClause.maxNumberOfAppeal) {
                println("In clause ${numberOfClause + 1} RAM size must be less than max number of appeal!")
                continue
            }
            File(outputFile).appendText("Clause ${numberOfClause + 1}:\n\n")
            outputTheResultOfAlgorithm(Process.FIFO, algorithm(Process.FIFO, curClause), outputFile)
            outputTheResultOfAlgorithm(Process.LRU, algorithm(Process.LRU, curClause), outputFile)
            outputTheResultOfAlgorithm(Process.OPT, algorithm(Process.OPT, curClause), outputFile)
        }
    }
    catch (e: IndexOutOfBoundsException) {
        println("You are trying to run the program with not enough arguments!")
    }
    catch (e: FileNotFoundException) {
        println("Missing input file!")
    }
    catch (e: NumberFormatException) {
        println("At least one of your arguments can't be parsed as Int!")
    }
}