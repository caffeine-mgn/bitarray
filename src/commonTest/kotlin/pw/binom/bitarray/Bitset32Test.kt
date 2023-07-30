package pw.binom.bitarray

import kotlin.test.assertEquals

class Bitset32Test : AbstractBitArrayTest() {
    override fun makeNew(): BitArray = BitArray32()

    override fun toStringTest() {
        var set = BitArray32()
        set = set.update(0, true)
        set = set.update(1, true)
        set = set.update(31, true)
        assertEquals("11000000000000000000000000000001", set.toString())
    }
}
