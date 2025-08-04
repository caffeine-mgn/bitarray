package pw.binom.bitarray

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.jvm.JvmInline

@JvmInline
value class ShortsBitArray(val data: ShortArray) : MutableBitArray {

    override val sizeInBytes: Int
        get() = data.size * Short.SIZE_BYTES

    /**
     * Size of array in bytes. For example `IntBitArray(2)` creates BytesBitArray with 64 elements
     */
    constructor(shortSize: Int) : this(ShortArray(shortSize))

    override fun set(index: Int, value: Boolean) {
        val externalIndex = index / Int.SIZE_BITS
        val internalIndex = index % Int.SIZE_BITS
        data[externalIndex] = data[externalIndex].update(index = internalIndex, value = value)
    }

    override fun inv() = ShortsBitArray(ShortArray(data.size) { data[it].inv() })

    override fun invert() {
        for (i in data.indices) {
            data[i] = data[i].inv()
        }
    }

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
            super.full(value = value, startIndex = startIndex, endIndex = endIndex)
        }
    }

    override fun copy() = ShortsBitArray(data.copyOf())

    override val size: Int
        get() = data.size * Short.SIZE_BITS

    override fun get(index: Int): Boolean =
        data[index / Short.SIZE_BITS][index % Short.SIZE_BITS]

    override fun isEmpty(): Boolean = data.isEmpty()

    override fun toString(): String {
        val sb = StringBuilder(size)
        data.forEach { byte ->
            byte.toBitsetString(sb)
        }
        return sb.toString()
    }

    infix fun and(other: ShortsBitArray): ShortsBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return ShortsBitArray(
            ShortArray(data.size) { index ->
                data[index] and other.data[index]
            },
        )
    }

    infix fun or(other: ShortsBitArray): ShortsBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return ShortsBitArray(
            ShortArray(data.size) { index ->
                data[index] or other.data[index]
            },
        )
    }

    infix fun xor(other: ShortsBitArray): ShortsBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return ShortsBitArray(
            ShortArray(data.size) { index ->
                data[index] xor other.data[index]
            },
        )
    }

    fun addAll(other: ShortsBitArray): ShortsBitArray {
        require(data.size == other.data.size) { "Size of BitArray should be equals" }
        repeat(data.size) { index ->
            data[index] = data[index] or other.data[index]
        }
        return this
    }

    override fun and(other: BitArray) = when (other) {
        is ShortsBitArray -> and(other)
        else -> super.and(other)
    }

    override fun or(other: BitArray) = when (other) {
        is ShortsBitArray -> or(other)
        else -> super.and(other)
    }

    override fun xor(other: BitArray) = when (other) {
        is ShortsBitArray -> xor(other)
        else -> super.and(other)
    }

    override fun addAll(other: BitArray) = when (other) {
        is ShortsBitArray -> addAll(other)
        else -> super.and(other)
    }

    override fun eachTrue(func: (Int) -> Unit) {
        data.forEachIndexed { index, l ->
            if (l != 0.toShort()) {
                var raw = l
                for (i in 0 until Int.SIZE_BITS) {
                    if ((raw and 0x1.toShort()) != 0.toShort()) {
                        func(i + index * Int.SIZE_BITS)
                    }
                    raw = raw ushr 1
                }
            }
        }
    }

    fun toBytesBitArray(): BytesBitArray {
        return BytesBitArray(NativeUtils.shortArrayToByteArray(data))
//        val output = ByteArray(data.size * Long.SIZE_BYTES)
//        data.forEachIndexed { index, l ->
//            var longValue = l
//            repeat(Long.SIZE_BYTES) { r ->
//                output[index * Long.SIZE_BYTES + (7 - r)] = (longValue and 0xFF).toByte()
//                longValue = longValue ushr 8
//            }
//        }
//        return BytesBitArray(output)
    }
}
