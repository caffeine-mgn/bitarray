package pw.binom.bitarray

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.jvm.JvmInline

/**
 * Implements bitset based on [Int]. Index 0 is the most left bit. For example: index 30 in value 0b010 is 1, index 31 and 29 is 0
 *
 * Example:
 * ```
 * var data = Bitset8()
 * assertFalse(data[0])
 * assertFalse(data[1])
 * data = data.set(0,true)
 * assertTrue(data[0])
 * assertFalse(data[1])
 * ```
 */
@JvmInline
value class BitArray8(val value: Byte = 0) : BitArray {
    override val size: Int
        get() = Byte.SIZE_BITS

    override val sizeInBytes: Int
        get() = Byte.SIZE_BYTES

    override fun fulled(value: Boolean, startIndex: Int, endIndex: Int): BitArray16 {
        if (startIndex == 0 && endIndex >= Short.SIZE_BITS - 1) {
            val numberValue = when (value) {
                true -> 0xFFFFu.toShort()
                false -> 0
            }
            return BitArray16(numberValue)
        }
        return super.fulled(value, startIndex, endIndex) as BitArray16
    }

    override operator fun get(index: Int): Boolean {
        val v = (MAX_BITS_1 - index)
        return value and (1 shl v).toByte() != 0.toByte()
    }

    override fun isEmpty(): Boolean = false
    override fun copy() = BitArray8(value)

    override fun update(index: Int, value: Boolean) = BitArray8(
        if (value) {
            (this.value or (1 shl (MAX_BITS_1 - index)).toByte())
        } else {
            (this.value.inv() or (1 shl MAX_BITS_1 - index).toByte()).inv()
        },
    )

    override fun inv() = BitArray8(value.inv())

    infix fun and(other: BitArray8): BitArray8 {
        require(other.size == size) { EQUALS_SIZE_ERROR }
        return BitArray8(value and other.value)
    }

    infix fun or(other: BitArray8): BitArray8 {
        require(other.size == size) { EQUALS_SIZE_ERROR }
        return BitArray8(value or other.value)
    }

    infix fun xor(other: BitArray8): BitArray8 {
        require(other.size == size) { EQUALS_SIZE_ERROR }
        return BitArray8(value xor other.value)
    }

    fun toByte() = value
    fun toUByte() = toByte().toUByte()
    override fun getByte4(index: Int) = ((value ushr (MAX_BITS - 4 - index)) and 0xF).toByte()
    override fun updateByte4(index: Int, value: Byte): BitArray8 {
        require(value <= 0xF)
        val leftPart = (this.value ushr (MAX_BITS_1 - index)) shl (MAX_BITS_1 - index)
        val rightPart = (this.value shl (index + 4)) ushr (index + 4)
        val valueInt = (value and 0xF) shl (MAX_BITS - 4 - index)
        return BitArray8(leftPart or valueInt or rightPart)
    }

    override fun eachTrue(func: (Int) -> Boolean) {
        var raw = value.toInt()
        for (i in 0 until size) {
            if ((raw and 0x1) != 0) {
                func(i)
            }
            raw = raw ushr 1
        }
    }

    override fun eachFalse(func: (Int) -> Boolean) {
        var raw = value.toInt()
        for (i in 0 until size) {
            if ((raw and 0x1) == 0) {
                func(i)
            }
            raw = raw ushr 1
        }
    }

    override fun getByte8(index: Int): Byte = ((value ushr (size - 8 - index)) and 0xFF.toByte()).toByte()

    /**
     * Returns value as unsigned int in radix 2
     */
    override fun toString(): String = value.toBitsetString()

    override fun and(other: BitArray): BitArray = when (other) {
        is BitArray16 -> and(other)
        else -> super.and(other)
    }

    override fun or(other: BitArray): BitArray = when (other) {
        is BitArray16 -> or(other)
        else -> super.and(other)
    }

    override fun xor(other: BitArray): BitArray = when (other) {
        is BitArray16 -> xor(other)
        else -> super.and(other)
    }
}

private infix fun Short.shl(i: Int) = (toInt() shl i).toShort()
private infix fun Short.shr(i: Int) = (toInt() shr i).toShort()
private infix fun Short.ushr(i: Int) = (toInt() ushr i).toShort()

fun Byte.toBitset() = BitArray8(this)

/**
 * Returns short as bit set string. Example:
 * value = 0b00110100, result=00000000000000000000000000110100
 */
private fun Byte.toBitsetString(): String {
    val leftPart = toUByte().toString(2)
    var len = MAX_BITS - leftPart.length
    val sb = StringBuilder()
    while (len > 0) {
        len--
        sb.append("0")
    }
    sb.append(leftPart)
    return sb.toString()
}

private const val MAX_BITS = Byte.SIZE_BITS
private const val MAX_BITS_1 = MAX_BITS - 1
