package pw.binom.bitarray

class BitArrayListIteratorImpl(cursor: Int, val sizeProvider: () -> Int, val dataProvider: (Int) -> Boolean) :
    BitArrayListIterator(cursor = cursor) {
    override val size: Int
        get() = sizeProvider()

    override fun get(index: Int): Boolean = dataProvider(index)
}
