package pw.binom.bitarray

import kotlin.test.assertEquals

class Bitset64Test : AbstractBitArrayTest() {
    override fun makeNew(): BitArray = BitArray64()

    override fun toStringTest() {
        var set = BitArray64()
        set = set.update(0, true)
        set = set.update(1, true)
        set = set.update(31, true)
        set = set.update(61, true)
        assertEquals("1100000000000000000000000000000100000000000000000000000000000100", set.toString())
    }
}
