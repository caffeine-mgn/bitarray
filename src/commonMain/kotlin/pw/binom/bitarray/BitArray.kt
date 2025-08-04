package pw.binom.bitarray

import kotlin.experimental.or

sealed interface BitArray : Iterable<Boolean>, RandomAccess, List<Boolean> {
    /**
     * Size of collection in bytes. For example [BitArray8] value of [sizeInBytes] is `1`.
     * For [BitArray32] value of [sizeInBytes] is `4`
     */
    val sizeInBytes: Int

    val lastIndex: Int
        get() = size - 1

    /**
     * Size of collection. For example [BitArray8] value of [size] is `8`.
     * For [BitArray32] value of [sizeInBytes] is `32`
     */
    abstract override val size: Int

    /**
     * Makes copy of current array and returns it
     */
    fun copy(): BitArray
    fun update(index: Int, value: Boolean): BitArray
    fun toBooleanArray() = BooleanArray(size) { this[it] }
    override fun isEmpty(): Boolean = size == 0

    /** Performs a bitwise AND operation between the two values. */
    infix fun and(other: BitArray): BitArray {
        require(other.size == size) { EQUALS_SIZE_ERROR }
        var ret = copy()
        for (index in 0 until size) {
            ret = ret.update(index = index, value = this[index] && other[index])
        }
        return ret
    }

    /** Performs a bitwise OR operation between the two values. */
    infix fun or(other: BitArray): BitArray {
        require(other.size == size) { EQUALS_SIZE_ERROR }
        var ret = copy()
        for (index in 0 until size) {
            ret = ret.update(index = index, value = this[index] || other[index])
        }
        return ret
    }

    /** Performs a bitwise XOR operation between the two values. */
    infix fun xor(other: BitArray): BitArray {
        require(other.size == size) { EQUALS_SIZE_ERROR }
        var ret = copy()
        for (index in 0 until size) {
            ret = ret.update(index = index, value = this[index] xor other[index])
        }
        return ret
    }

    /** Inverts the bits in this value. */
    operator fun inv(): BitArray {
        var out = copy()
        for (index in 0 until size) {
            out = out.update(index, this[index])
        }
        return out
    }

    /**
     * Creates new array, and then set [value] to all indexes betweeb [startIndex] and [endIndex]. Then return result
     */
    fun fulled(value: Boolean, startIndex: Int = 0, endIndex: Int = lastIndex): BitArray {
        var self = this
        for (i in startIndex..lastIndex) {
            self = self.update(i, value)
        }
        return self
    }

    /**
     * Returns byte from 4 bites using [index]
     *
     * @param index Offset for getting byte
     */
    fun getByte4(index: Int): Byte {
        var result = 0.toByte()
        for (i in 0 until 4) {
            if (this[index + i]) {
                result = (result or 1.toByte() shl (3 - i))
            }
        }
        return result
    }

    /**
     * Returns byte from 8 bites using [index]
     *
     * Example:
     * ```
     * val set = Bitset32(0b110101)// 1101 + 01
     * println(set.getByte(6)) //will print "1101". 6 offset = 4 bits of byte + 2 offset
     * ```
     *
     * @param index Offset for getting byte
     */
    fun getByte8(index: Int): Byte {
        var result = 0.toByte()
        for (i in 0 until Byte.SIZE_BITS) {
            if (this[index + i]) {
                result = (result + 1.toByte() shl (Byte.SIZE_BITS - 1 - i)).toByte()
            }
        }
        return result
    }

    /**
     * Sets 4 bits [value] to this inline value using offset [index]. [value] must be less or equals 0xF
     *
     * Example:
     * ```
     * val set = Bitset64(0b100000) // 10 + 0000
     * set = set.setByte4(4, 0b1011)
     * println(set.toString()) // will print "101011". 10 + 1011
     * ```
     *
     * @param index offset for set data
     * @param value new value
     */
    fun updateByte4(index: Int, value: Byte): BitArray {
        var r = this
        for (i in 0 until 4) {
            r = r.update(index + i, value[i + 4])
        }
        return r
    }

    fun updateByte8(index: Int, value: Byte): BitArray {
        var r = this
        for (i in 0 until 8) {
            r = r.update(index + i, value[i])
        }
        return r
    }

    override fun indexOf(element: Boolean): Int {
        for (i in 0 until size) {
            if (this[i] == element) {
                return i
            }
        }
        return -1
    }

    override fun lastIndexOf(element: Boolean): Int {
        for (i in size - 1 downTo 0) {
            if (this[i] == element) {
                return i
            }
        }
        return -1
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Boolean> {
        val min = maxOf(fromIndex, 0)
        val max = minOf(toIndex, size)
        if (max - min == 0) {
            return emptyList()
        }
        val output = ArrayList<Boolean>(max - min)
        for (i in min until max) {
            output += this[i]
        }
        return output
    }

    override fun contains(element: Boolean): Boolean = indexOf(element) >= 0

    override fun containsAll(elements: Collection<Boolean>): Boolean {
        elements.forEach { el ->
            if (!contains(el)) {
                return false
            }
        }
        return true
    }

    override fun listIterator(): ListIterator<Boolean> = listIterator(0)
    override fun iterator() = listIterator()
    override fun listIterator(index: Int) = BitArrayListIteratorImpl(
        cursor = index,
        sizeProvider = { this@BitArray.size },
        dataProvider = { this@BitArray[it] },
    )

    fun eachTrue(func: (Int) -> Unit) {
        forEachIndexed { index, value ->
            if (value) {
                func(index)
            }
        }
    }

    fun eachFalse(func: (Int) -> Unit) {
        forEachIndexed { index, value ->
            if (!value) {
                func(index)
            }
        }
    }
}
