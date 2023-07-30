package pw.binom.bitarray

import kotlin.test.Test
import kotlin.test.assertTrue

abstract class AbstractMutableBitArrayTest : AbstractBitArrayTest() {
    abstract override fun makeNew(): MutableBitArray

    @Test
    fun clearTest() {
        val arr = makeNew()
        arr[1] = true
        arr[3] = true
        arr.clear()
        assertTrue(arr.all { !it })
    }

    @Test
    fun full1Test() {
        val arr = makeNew()
        arr.full(true, startIndex = 0, endIndex = arr.lastIndex)
        assertTrue(arr.all { it })
    }

    @Test
    fun full2Test() {
        val arr = makeNew()
        arr.full(false, startIndex = 0, endIndex = arr.lastIndex)
        assertTrue(arr.all { !it })
    }
}
