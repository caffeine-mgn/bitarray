package pw.binom

import kotlin.test.assertTrue

class LongsBitsetTest : AbstractBitArrayTest() {
    override fun makeNew() = LongsBitArray(LongArray(4))

    override fun toStringTest() {
        val b = makeNew()
        b[0] = true
        b[1] = true
        b[3] = true
        b[7] = true
        assertTrue(b.toString().startsWith("110100010000"))
    }
}
