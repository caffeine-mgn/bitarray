package pw.binom.bitarray

import kotlin.test.assertEquals

class Bitset8Test : AbstractBitArrayTest() {
    override fun makeNew(): BitArray = BitArray8()

    override fun toStringTest() {
        var set = BitArray8()
        set = set.update(0, true)
        set = set.update(1, true)
        set = set.update(7, true)
        assertEquals("11000001", set.toString())
    }

    override fun getByte4Test() {
        // Do nothing
    }

    override fun getByte8Test() {
        // Do nothing
    }
}
