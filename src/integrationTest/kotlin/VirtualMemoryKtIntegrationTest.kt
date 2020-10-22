import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class VirtualMemoryKtIntegrationTest {

    @Test
    fun test00() {
        main(arrayOf("data/Input00.txt", "output.txt"))
        assertEquals(File("data/Output00.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test01() {
        main(arrayOf("data/Input01.txt", "output.txt"))
        assertEquals(File("data/Output01.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test02() {
        main(arrayOf("data/Input02.txt", "output.txt"))
        assertEquals(File("data/Output02.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test03() {
        main(arrayOf("data/Input03.txt", "output.txt"))
        assertEquals(File("data/Output03.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test04() {
        main(arrayOf("data/Input04.txt", "output.txt"))
        assertEquals(File("data/Output04.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test05() {
        main(arrayOf("data/Input05.txt", "output.txt"))
        assertEquals(File("data/Output05.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test06() {
        main(arrayOf("data/Input06.txt", "output.txt"))
        assertEquals(File("data/Output06.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test07() {
        main(arrayOf("data/Input07.txt", "output.txt"))
        assertEquals(File("data/Output07.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test08() {
        main(arrayOf("data/Input08.txt", "output.txt"))
        assertEquals(File("data/Output08.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test09() {
        main(arrayOf("data/Input09.txt", "output.txt"))
        assertEquals(File("data/Output09.txt").readLines(), File("output.txt").readLines())
    }

    @Test
    fun test10() {
        main(arrayOf("data/Input10.txt", "output.txt"))
        assertEquals(File("data/Output10.txt").readLines(), File("output.txt").readLines())
    }
}