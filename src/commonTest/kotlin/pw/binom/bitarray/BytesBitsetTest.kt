package pw.binom.bitarray

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BytesBitsetTest : AbstractMutableBitArrayTest() {
    override fun makeNew() = BytesBitArray(ByteArray(8))

    override fun toStringTest() {
        val b = makeNew()
        b[0] = true
        b[1] = true
        b[3] = true
        b[7] = true
        assertEquals("1101000100000000000000000000000000000000000000000000000000000000", b.toString())
    }

    @Test
    fun toLongBitArrayTest() {
        val data = makeNew()
        data[0] = true
        data[7] = true
        data[13] = true
        data[15] = true

        val bytes = data.toLongsBitArray()
        println("data=$data")
        println("bytes=$bytes")
        assertContentEquals(data, bytes, "#1")
    }
}
