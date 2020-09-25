import java.util.*

data class Replacement(val frameNumber: Int, val replacedValue: Int, val newValue: Int)

val m = 3
val n = 10

fun getNumberOfReplacedFrame(curReplacement: Int) = if (curReplacement % m == 0) m else curReplacement % m

fun FIFO(appeals: List<Int>): Pair<Int, List<Replacement>> {
    val memory: Queue<Int> = LinkedList<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for (curPage in appeals) {
        if (memory.contains(curPage)) {
            replacements.add(Replacement(0, curPage, curPage)) // 0 means that the needed page is in memory
        } else {
            numberOfReplacements++
            var replacedValue = 0
            if (memory.size == m) {
                replacedValue = memory.element()
                memory.remove()
            }
            memory.add(curPage)
            replacements.add(Replacement(getNumberOfReplacedFrame(numberOfReplacements), replacedValue, curPage))
        }
    }
    return Pair(numberOfReplacements, replacements)
}

fun LRU(appeals: List<Int>): Pair<Int, List<Replacement>> {
    val memory = mutableListOf<Int>()
    val pretendentsForReplacement = mutableListOf<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for (curPage in appeals) {
        if (memory.contains(curPage)) {
            replacements.add(Replacement(0, curPage, curPage)) // 0 means that the needed page is in memory
            pretendentsForReplacement.remove(curPage)
            pretendentsForReplacement.add(curPage)
        } else {
            numberOfReplacements++
            var replacedValue = 0
            var replacedFrame = getNumberOfReplacedFrame(numberOfReplacements)
            if (memory.size == m) {
                replacedValue = pretendentsForReplacement[0]
                pretendentsForReplacement.remove(pretendentsForReplacement[0])
                replacedFrame = memory.indexOf(replacedValue) + 1
                pretendentsForReplacement.add(curPage)
                memory[replacedFrame - 1] = curPage
            } else {
                memory.add(curPage)
                pretendentsForReplacement.add(curPage)
            }
            replacements.add(Replacement(replacedFrame, replacedValue, curPage))
        }
    }
    return Pair(numberOfReplacements, replacements)
}

fun main(args: Array<String>) {
    val appeals = listOf(1, 2, 3, 4, 5, 5, 4, 3, 2, 1)
    val FIFOresult = FIFO(appeals)
    println("FIFO: ${FIFOresult.first} replacements")
    for (replacement in FIFOresult.second) {
        if (replacement.frameNumber == 0) {
            println("${replacement.newValue}: The needed page is in memory")
            continue
        }
        if (replacement.replacedValue == 0){
            println("Add ${replacement.newValue} in ${replacement.frameNumber} cell")
        } else {
            println("Replace ${replacement.replacedValue} with ${replacement.newValue} in ${replacement.frameNumber} cell")
        }
    }
    println("")
    val LRUresult = LRU(appeals)
    println("LRU: ${LRUresult.first} replacements")
    for (replacement in LRUresult.second) {
        if (replacement.frameNumber == 0) {
            println("${replacement.newValue}: The needed page is in memory")
            continue
        }
        if (replacement.replacedValue == 0){
            println("Add ${replacement.newValue} in ${replacement.frameNumber} cell")
        } else {
            println("Replace ${replacement.replacedValue} with ${replacement.newValue} in ${replacement.frameNumber} cell")
        }
    }
}