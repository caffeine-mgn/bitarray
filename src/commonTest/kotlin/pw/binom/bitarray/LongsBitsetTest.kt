package pw.binom.bitarray

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class LongsBitsetTest : AbstractMutableBitArrayTest() {
    override fun makeNew() = LongsBitArray(LongArray(4))

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
        val l = makeNew()
        l[0] = true
        l[7] = true
        l[13] = true
        l[15] = true
        l[44] = true

        val bytes = l.toBytesBitArray()
        assertContentEquals(l, bytes)
        assertContentEquals(l, bytes.toLongsBitArray())
    }
}
