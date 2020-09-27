import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VirtualMemoryKtTest {

    @Test
    fun `simple test for parseForMN`() {
        assertEquals(Pair(3, 5), parseForMN("3 5"))
    }

    @Test
    fun `another simple test for parseForMN`() {
        assertEquals(Pair(10, 15), parseForMN("10 15"))
    }

    @Test
    fun `simple test for parseForAppeals`() {
        assertEquals(listOf(1, 2, 3, 4, 5), parseForAppeals("1 2 3 4 5"))
    }

    @Test
    fun `test with repeatable appeals for parseForAppeals`() {
        assertEquals(listOf(1, 2, 3, 3, 1, 2), parseForAppeals("1 2 3 3 1 2"))
    }

    @Test
    fun `test with no appeals for parseForClause`() {
        assertEquals(Clause(3, 5, listOf()), parseForClause("3 5", ""))
    }

    @Test
    fun `test with appeals for parseForClause`() {
        val expected = Clause(3, 5, listOf(1, 2, 3, 4, 5, 3, 2, 3, 5, 1))
        val actual = parseForClause("3 5", "1 2 3 4 5 3 2 3 5 1")
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
    fun `simple test for getNumberOfReplacedFrame`() {
        assertEquals(2, getNumberOfReplacedFrame(2, 3))
    }

    @Test
    fun `test when current replacement number is more than m for getNumberOfReplacedFrame`() {
        assertEquals(2, getNumberOfReplacedFrame(6, 4))
    }

    @Test
    fun `test when current replacement number equals m for getNumberOfReplacedFrame`() {
        assertEquals(4, getNumberOfReplacedFrame(4, 4))
    }

    @Test
    fun `simple test for findReplacedFrameFIFO`() {
        assertEquals(3, findReplacedFrameFIFO(listOf(4, 5, 0, 1, 2), 5))
    }

    @Test
    fun `simple test for findReplacedFrameLRU`() {
        assertEquals(3, findReplacedFrameLRU(5, Clause(3, 5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1)), listOf(4, 5, 3)))
    }

    @Test
    fun `simple test for findReplacedFrameOPT`() {
        assertEquals(2, findReplacedFrameOPT(3, Clause(3, 5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1)), listOf(1, 2, 3)))
    }

    @Test
    fun `FIFO test for algorithm`() {
        assertEquals(8, algorithm("FIFO", Clause(3,5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1))).first)
    }

    @Test
    fun `LRU test for algorithm`() {
        assertEquals(9, algorithm("LRU", Clause(3,5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1))).first)
    }

    @Test
    fun `OPT test for algorithm`() {
        assertEquals(6, algorithm("OPT", Clause(3,5, listOf(1, 2, 3, 4, 5, 1, 3, 5, 2, 1))).first)
    }
}