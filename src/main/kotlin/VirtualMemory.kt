import java.util.*

data class Replacement(val cellNumber: Int, val replacedValue: Int, val newValue: Int)

val m = 3
val n = 10

fun getNumberOfReplacedCell(curReplacement: Int) = if (curReplacement % m == 0) m else curReplacement % m

fun FIFO(appeals: List<Int>): Pair<Int, List<Replacement>> {
    val memory: Queue<Int> = LinkedList<Int>()
    var numberOfReplacements = 0
    val replacements = mutableListOf<Replacement>()
    for (curPage in appeals) {
        if (memory.contains(curPage)) {
            replacements.add(Replacement(0, curPage, curPage)) // 0 means that the needed page is in memory
            continue
        } else {
            var replacedValue = 0
            if (memory.size == m) {
                replacedValue = memory.element()
                memory.remove()
            }
            memory.add(curPage)
            numberOfReplacements++
            replacements.add(Replacement(getNumberOfReplacedCell(numberOfReplacements), replacedValue, curPage))
        }
    }
    return Pair(numberOfReplacements, replacements)
}

fun main(args: Array<String>) {
    val appeals = listOf(1, 2, 3, 4, 5, 5, 4, 3, 2, 1)
    val FIFOresult = FIFO(appeals)
    println("FIFO: ${FIFOresult.first} replacements")
    for (replacement in FIFOresult.second) {
        if (replacement.cellNumber == 0) {
            println("${replacement.newValue}: The needed page is in memory")
            continue
        }
        if (replacement.replacedValue == 0){
            println("Add ${replacement.newValue} in ${replacement.cellNumber} cell")
        } else {
            println("Replace ${replacement.replacedValue} with ${replacement.newValue} in ${replacement.cellNumber} cell")
        }
    }
}