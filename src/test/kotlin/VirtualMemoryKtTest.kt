import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VirtualMemoryKtTest {

    @Test
    fun `test with appeals for parseClause`() {
        val expected = Clause(3, 5, listOf(1, 2, 3, 4, 5, 3, 2, 3, 5, 1))
        val actual = parseClause("3 5", "1 2 3 4 5 3 2 3 5 1")
        assertEquals(expected, actual)
    }

    @Test
    fun `test with one clause for parseTheInput`() {
        val expected = listOf(Clause(3, 5, listOf(1, 2, 3, 4, 5, 3, 2, 3, 5, 1)))
        val actual = parseTheInput(listOf("3 5", "1 2 3 4 5 3 2 3 5 1"))
        assertEquals(expected, actual)
    }

    @Test
    fun `test with several clauses for parseTheInput`() {
        val expected = listOf(Clause(3, 5, listOf(1, 2, 3, 4, 5, 3, 2, 3, 5, 1)), Clause(1, 2, listOf(1, 2, 2, 1)))
        val actual = parseTheInput(listOf("3 5", "1 2 3 4 5 3 2 3 5 1", "1 2", "1 2 2 1"))
        assertEquals(expected, actual)
    }

    @Test
    fun `test for getNextAppeal`() {
        assertEquals(listOf(5, 8, 6, 14, 7, 9, 13, 15, 12, 11), getNextAppeal(Clause(3, 5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1))))
    }

    @Test
    fun `simple test for findReplacedFrameFIFO`() {
        assertEquals(3, Process.FIFO.findReplacedFrame(listOf(5, 6, 3, 4), mutableListOf(3, 4, 5, 6)))
    }

    @Test
    fun `simple test for findReplacedFrameLRU`() {
        assertEquals(3, Process.LRU.findReplacedFrame(listOf(1, 2, 3, 4), mutableListOf(3, 1, 2, 4)))
    }

    @Test
    fun `simple test for findReplacedFrameOPT`() {
        assertEquals(2, Process.OPT.findReplacedFrame(listOf(1, 2, 3, 4), mutableListOf(4, 7, 6, 5)))
    }

    @Test
    fun `FIFO test for algorithm`() {
        assertEquals(8, algorithm(Process.FIFO, Clause(3,5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1))).first)
    }

    @Test
    fun `LRU test for algorithm`() {
        assertEquals(9, algorithm(Process.LRU, Clause(3,5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1))).first)
    }

    @Test
    fun `OPT test for algorithm`() {
        assertEquals(6, algorithm(Process.OPT, Clause(3,5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1))).first)
    }
}