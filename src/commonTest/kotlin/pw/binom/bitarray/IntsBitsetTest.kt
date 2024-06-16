package pw.binom.bitarray

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class IntsBitsetTest : AbstractMutableBitArrayTest() {
    override fun makeNew() = IntsBitArray(IntArray(8))

    override fun toStringTest() {
        val b = makeNew()
        b[0] = true
        b[1] = true
        b[3] = true
        b[7] = true
        assertTrue(b.toString().startsWith("110100010000"))
    }

    @Test
    fun toByteBitArrayTest() {
        val data = makeNew()
        data[0] = true
        data[7] = true
        data[13] = true
        data[15] = true
        data[44] = true

        val bytes = data.toBytesBitArray()
        println("data.size=${data.size}")
        println("bytes.size=${bytes.size}")
        println("data =$data")
        println("bytes=$bytes")
        assertContentEquals(data, bytes, "#1")
    }
}
