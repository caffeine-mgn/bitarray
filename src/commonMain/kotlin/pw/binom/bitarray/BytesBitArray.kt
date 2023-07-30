package pw.binom.bitarray

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.jvm.JvmInline

@JvmInline
value class BytesBitArray(val data: ByteArray) : MutableBitArray {

    /**
     * Size of array in bytes. For example `BytesBitArray(2)` creates BytesBitArray with 16 elements
     */
    constructor(byteSize: Int) : this(ByteArray(byteSize))

    override operator fun get(index: Int): Boolean {
        val value = data[index / Byte.SIZE_BITS]
        val mask = 1.toByte() shl (Byte.SIZE_BITS - 1 - (index % Byte.SIZE_BITS))
        return value and mask != 0.toByte()
    }

    override fun isEmpty(): Boolean = data.isEmpty()

    override fun update(index: Int, value: Boolean): BytesBitArray {
        val result = BytesBitArray(data.copyOf())
        result[index] = value
        return result
    }

    override fun inv() = BytesBitArray(ByteArray(data.size) { data[it].inv() })
    override fun invert() {
        for (i in data.indices) {
            data[i] = data[i].inv()
        }
    }

    override fun getByte4(index: Int): Byte {
        val byteIndex = index / Byte.SIZE_BITS
        val value = data[byteIndex]
        if (index % Byte.SIZE_BITS == 0) {
            return value ushr 4
        }
        if (index % 4 == 0) {
            return value and 0xF
        }
        val mod = index % Byte.SIZE_BITS
        if (mod <= 4) {
            return value ushr Byte.SIZE_BITS - 4 - mod
        }
        val value2 = data[byteIndex + 1]
        val v1 = value shl (4 - (Byte.SIZE_BITS - mod))
        val v2 = value2 ushr (Byte.SIZE_BITS - ((mod + 4) % Byte.SIZE_BITS))
        return (v1 + v2).toByte()
    }

    override fun setByte4(index: Int, value: Byte) {
        val byteIndex = index / Byte.SIZE_BITS
        val oldValue = data[byteIndex]
        val bitOffset = index % Byte.SIZE_BITS
        if (bitOffset == 0) {
            data[byteIndex] = ((oldValue ushr 4) + (value shl 4)).toByte()
            return
        }
        if (index % 4 == 0) {
            data[byteIndex] = (((oldValue ushr 4) shl 4) + value and 0xF).toByte()
            return
        }

        if (bitOffset < 4) {
            val l = 4 + (4 - bitOffset)
            val r = 4 + bitOffset
            val insertValue = value shl (4 - bitOffset)
            val left = (oldValue ushr l) shl l
            val right = (oldValue shl r) ushr r
            val result = (left + insertValue + right).toByte()
            data[byteIndex] = result
            return
        }

        val left = (((oldValue ushr bitOffset) shl bitOffset) + ((value and 0xF) ushr (4 - bitOffset))).toByte()
        data[byteIndex] = left
        val oldValue2 = data[byteIndex + 1]

        val valueRight = value shl (4 + bitOffset)
        val right = (((oldValue2 shl bitOffset) ushr bitOffset) + valueRight).toByte()
        data[byteIndex + 1] = right
    }

    override fun updateByte4(index: Int, value: Byte): BytesBitArray {
        val ret = BytesBitArray(data.copyOf())
        ret.setByte4(index = index, value = value)
        return ret
    }

    override fun getByte8(index: Int): Byte {
        val mod = index % Byte.SIZE_BITS
        val byteIndex = index / Byte.SIZE_BITS
        if (mod == 0) {
            return data[byteIndex]
        }
        val value1 = data[byteIndex]
        val value2 = data[byteIndex + 1]
        val leftPart = (value1 shl mod)
        val rightPart = value2 ushr (Byte.SIZE_BITS - mod)
        return (leftPart + rightPart).toByte()
    }

    override fun copy() = BytesBitArray(data.copyOf())
    override fun clear() {
        for (i in 0 until data.size) {
            data[i] = 0
        }
    }

    override fun full(value: Boolean, startIndex: Int, endIndex: Int) {
        if (startIndex == 0 && endIndex == lastIndex) {
            if (value) {
                for (i in 0 until data.size) {
                    data[i] = -1
                }
            } else {
                clear()
            }
        } else {
            super.full(value, startIndex, endIndex)
        }
    }

    override operator fun set(index: Int, value: Boolean) {
        val value1 = data[index / Byte.SIZE_BITS].toInt() and 0xFF
        val t = 1 shl (Byte.SIZE_BITS - 1 - index % Byte.SIZE_BITS)
        val newValue = if (value) {
            value1 or t
        } else {
            (value1.inv() or t).inv()
        }
        data[index / Byte.SIZE_BITS] = newValue.toByte()
    }

    override val size
        get() = data.size * Byte.SIZE_BITS

    override fun toString(): String {
        val sb = StringBuilder(size)
        data.forEach { byte ->
            sb.append(byte.toUByte().toString(2).padStart(length = Byte.SIZE_BITS, padChar = '0'))
        }
        return sb.toString()
    }

    infix fun and(other: BytesBitArray): BytesBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return BytesBitArray(
            ByteArray(data.size) { index ->
                data[index] and other.data[index]
            },
        )
    }

    infix fun or(other: BytesBitArray): BytesBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return BytesBitArray(
            ByteArray(data.size) { index ->
                data[index] or other.data[index]
            },
        )
    }

    infix fun xor(other: BytesBitArray): BytesBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return BytesBitArray(
            ByteArray(data.size) { index ->
                data[index] xor other.data[index]
            },
        )
    }

    fun addAll(other: BytesBitArray): BytesBitArray {
        require(data.size == other.data.size) { "Size of BitArray should be equals" }
        repeat(data.size) { index ->
            data[index] = data[index] or other.data[index]
        }
        return this
    }

    override fun and(other: BitArray) = when (other) {
        is BytesBitArray -> and(other)
        else -> super.and(other)
    }

    override fun or(other: BitArray) = when (other) {
        is BytesBitArray -> or(other)
        else -> super.and(other)
    }

    override fun xor(other: BitArray) = when (other) {
        is BytesBitArray -> xor(other)
        else -> super.and(other)
    }

    override fun addAll(other: BitArray) = when (other) {
        is BytesBitArray -> addAll(other)
        else -> super.and(other)
    }

    fun toLongsBitArray(): LongsBitArray {
        val other = data.size % Long.SIZE_BYTES
        val validSize = if (other == 0) data.size else data.size - other + Long.SIZE_BYTES
        require(other == 0) { "Size of BytesBitArray should be $validSize" }
        val ret = LongArray(validSize / Long.SIZE_BYTES)
        var cursorInLong = 0
        while (cursorInLong * Long.SIZE_BYTES < data.size) {
            val cursorInBytes = cursorInLong * Long.SIZE_BYTES
            ret[cursorInLong++] = Long.fromBytes(
                byte0 = data[cursorInBytes + 0],
                byte1 = data[cursorInBytes + 1],
                byte2 = data[cursorInBytes + 2],
                byte3 = data[cursorInBytes + 3],
                byte4 = data[cursorInBytes + 4],
                byte5 = data[cursorInBytes + 5],
                byte6 = data[cursorInBytes + 6],
                byte7 = data[cursorInBytes + 7],
            )
        }
        return LongsBitArray(ret)
    }
}

/**
 * Creates and return [BytesBitArray] using this array as data.
 */
fun ByteArray.toBitset() = BytesBitArray(this)
