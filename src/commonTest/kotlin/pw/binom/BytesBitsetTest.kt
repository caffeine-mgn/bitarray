package pw.binom

import kotlin.test.Test
import kotlin.test.assertEquals

class BytesBitsetTest : AbstractBitArrayTest() {
    override fun makeNew() = BytesBitArray(ByteArray(4))

    override fun toStringTest() {
        val b = makeNew()
        b[0] = true
        b[1] = true
        b[3] = true
        b[7] = true
        assertEquals("11010001000000000000000000000000", b.toString())
    }
}
