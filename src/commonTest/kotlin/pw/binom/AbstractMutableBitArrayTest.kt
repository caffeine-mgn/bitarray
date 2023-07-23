package pw.binom

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
}
