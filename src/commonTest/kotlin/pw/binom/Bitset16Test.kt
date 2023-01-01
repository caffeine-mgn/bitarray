package pw.binom

import kotlin.test.assertEquals

class Bitset16Test : AbstractBitArrayTest() {
    override fun makeNew(): BitArray = BitArray16()

    override fun toStringTest() {
        var set = BitArray16()
        set = set.update(0, true)
        set = set.update(1, true)
        set = set.update(15, true)
        assertEquals("1100000000000001", set.toString())
    }
}
