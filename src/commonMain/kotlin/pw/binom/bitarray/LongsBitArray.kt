package pw.binom.bitarray

import kotlin.jvm.JvmInline

@JvmInline
value class LongsBitArray(val data: LongArray) : MutableBitArray {

    override val sizeInBytes: Int
        get() = data.size * Long.SIZE_BYTES

    /**
     * Size of array in bytes. For example `LongsBitArray(2)` creates BytesBitArray with 128 elements
     */
    constructor(longSize: Int) : this(LongArray(longSize))

    override fun set(index: Int, value: Boolean) {
        val externalIndex = index / Long.SIZE_BITS
        val internalIndex = index % Long.SIZE_BITS
        data[externalIndex] = data[externalIndex].update(index = internalIndex, value = value)
    }

    override fun inv() = LongsBitArray(LongArray(data.size) { data[it].inv() })

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

    override fun copy() = LongsBitArray(data.copyOf())

    override val size: Int
        get() = data.size * Long.SIZE_BITS

    override fun get(index: Int): Boolean =
        data[index / Long.SIZE_BITS][index % Long.SIZE_BITS]

    override fun isEmpty(): Boolean = data.isEmpty()

    override fun toString(): String {
        val sb = StringBuilder(size)
        data.forEach { byte ->
            byte.toBitsetString(sb)
        }
        return sb.toString()
    }

    infix fun and(other: LongsBitArray): LongsBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return LongsBitArray(
            LongArray(data.size) { index ->
                data[index] and other.data[index]
            },
        )
    }

    infix fun or(other: LongsBitArray): LongsBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return LongsBitArray(
            LongArray(data.size) { index ->
                data[index] or other.data[index]
            },
        )
    }

    infix fun xor(other: LongsBitArray): LongsBitArray {
        require(other.data.size == data.size) { EQUALS_SIZE_ERROR }
        return LongsBitArray(
            LongArray(data.size) { index ->
                data[index] xor other.data[index]
            },
        )
    }

    fun addAll(other: LongsBitArray): LongsBitArray {
        require(data.size == other.data.size) { "Size of BitArray should be equals" }
        repeat(data.size) { index ->
            data[index] = data[index] or other.data[index]
        }
        return this
    }

    override fun and(other: BitArray) = when (other) {
        is LongsBitArray -> and(other)
        else -> super.and(other)
    }

    override fun or(other: BitArray) = when (other) {
        is LongsBitArray -> or(other)
        else -> super.or(other)
    }

    override fun xor(other: BitArray) = when (other) {
        is LongsBitArray -> xor(other)
        else -> super.xor(other)
    }

    override fun addAll(other: BitArray) = when (other) {
        is LongsBitArray -> addAll(other)
        else -> super.addAll(other)
    }

    override fun eachTrue(func: (Int) -> Boolean) {
        data.forEachIndexed { index, l ->
            if (l != 0L) {
                var raw = l
                for (i in 0 until Long.SIZE_BITS) {
                    if ((raw and 0x1L) != 0L) {
                        func(i + index * Long.SIZE_BITS)
                    }
                    raw = raw ushr 1
                }
            }
        }
    }

    fun toBytesBitArray(): BytesBitArray {
        return BytesBitArray(NativeUtils.longArrayToByteArray(data))
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
