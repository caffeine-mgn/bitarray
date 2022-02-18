package pw.binom

import kotlin.jvm.JvmInline

@JvmInline
value class LongsBitArray(val data: LongArray) : MutableBitArray {

    /**
     * Size of array in bytes. For example `LongsBitArray(2)` creates BytesBitArray with 128 elements
     */
    constructor(longSize: Int) : this(LongArray(longSize))

    override fun set(index: Int, value: Boolean) {
        val externalIndex = index / Long.SIZE_BITS
        val internalIndex = index % Long.SIZE_BITS
        data[externalIndex] = data[externalIndex].update(index = internalIndex, value = value)
    }

    override fun invert() {
        for (i in data.indices) {
            data[i] = data[i].inv()
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
}
