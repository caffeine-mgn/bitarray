package pw.binom.bitarray

import kotlin.jvm.JvmInline

/**
 * Implements bitset based on [Long]. Index 0 is the most left bit. For example: index 30 in value 0b010 is 1, index 31 and 29 is 0
 *
 * Example:
 * ```
 * var data = Bitset64()
 * assertFalse(data[0])
 * assertFalse(data[1])
 * data = data.set(0,true)
 * assertTrue(data[0])
 * assertFalse(data[1])
 * ```
 */
@JvmInline
value class BitArray64(val value: Long = 0) : BitArray {
    companion object {
        val FULL = BitArray64(0xFFFFFFFFFFFFFFFFuL.toLong())
    }
    override val size: Int
        get() = Long.SIZE_BITS

    override val sizeInBytes: Int
        get() = Long.SIZE_BYTES

    override fun fulled(value: Boolean, startIndex: Int, endIndex: Int): BitArray64 {
        if (startIndex == 0 && endIndex >= Long.SIZE_BITS - 1) {
            val numberValue = when (value) {
                true -> 0xFFFFFFFFFFFFFFFFuL.toLong()
                false -> 0L
            }
            return BitArray64(numberValue)
        }
        return super.fulled(value, startIndex, endIndex) as BitArray64
    }

    override fun eachTrue(func: (Int) -> Boolean) {
        var raw = value
        for (i in 0 until size) {
            if ((raw and 0x1L) != 0L) {
                func(i)
            }
            raw = raw ushr 1
        }
    }

    override fun eachFalse(func: (Int) -> Boolean) {
        var raw = value
        for (i in 0 until size) {
            if ((raw and 0x1L) == 0L) {
                func(i)
            }
            raw = raw ushr 1
        }
    }

    override operator fun get(index: Int): Boolean = value[index] // value and (1L shl (MAX_BITS_1 - index)) != 0L
    override fun isEmpty(): Boolean = false

    override fun update(index: Int, value: Boolean) =
        BitArray64(this.value.update(index = index, value = value))

    override fun inv(): BitArray64 = BitArray64(value.inv())

    fun toLong() = value
    fun toULong() = toLong().toULong()
    override fun getByte4(index: Int) = ((value ushr (MAX_BITS - 4 - index)) and 0xF).toByte()

    override fun updateByte4(index: Int, value: Byte): BitArray64 {
        require(value <= 0xF)
        val leftPart = (this.value ushr (MAX_BITS_1 - index)) shl (MAX_BITS_1 - index)
        val rightPart = (this.value shl (index + 4)) ushr (index + 4)
        val valueInt = (value.toLong() and 0xF) shl (MAX_BITS - 4 - index)
        return BitArray64(leftPart or valueInt or rightPart)
    }

    override fun getByte8(index: Int): Byte =
        ((value ushr (size - 8 - index)) and 0xFF).toByte()

    /**
     * Returns value as unsigned int in radix 2
     */
    override fun toString(): String = value.toBitsetString()
    override fun copy() = BitArray64(value)

    infix fun and(other: BitArray64): BitArray64 {
        return BitArray64(value and other.value)
    }

    infix fun or(other: BitArray64): BitArray64 {
        return BitArray64(value or other.value)
    }

    infix fun xor(other: BitArray64): BitArray64 {
        return BitArray64(value xor other.value)
    }

    override fun and(other: BitArray): BitArray = when (other) {
        is BitArray64 -> and(other)
        else -> super.and(other)
    }

    override fun or(other: BitArray): BitArray = when (other) {
        is BitArray64 -> or(other)
        else -> super.and(other)
    }

    override fun xor(other: BitArray): BitArray = when (other) {
        is BitArray64 -> xor(other)
        else -> super.and(other)
    }
}

fun Long.toBitset() = BitArray64(this)

/**
 * Returns int as bit set string. Example:
 * value = 0b00110100, result=00000000000000000000000000110100
 */
// private fun Long.toBitsetString(): String {
//    val leftPart = toULong().toString(2)
//    var len = MAX_BITS - leftPart.length
//    val sb = StringBuilder()
//    while (len > 0) {
//        len--
//        sb.append("0")
//    }
//    sb.append(leftPart)
//    return sb.toString()
// }

private const val MAX_BITS = Long.SIZE_BITS
private const val MAX_BITS_1 = MAX_BITS - 1
