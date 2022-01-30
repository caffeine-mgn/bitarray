package pw.binom

abstract class BitArrayListIterator(private var cursor: Int) : ListIterator<Boolean> {
    protected abstract val size: Int
    protected abstract fun get(index: Int): Boolean
    override fun hasNext(): Boolean = cursor + 1 < size

    override fun hasPrevious(): Boolean = cursor - 1 >= 0

    override fun next(): Boolean {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return get(cursor++)
    }

    override fun nextIndex(): Int = cursor + 1

    override fun previous(): Boolean {
        if (!hasPrevious()) {
            throw NoSuchElementException()
        }
        return get(cursor--)
    }

    override fun previousIndex(): Int = cursor - 1
}
