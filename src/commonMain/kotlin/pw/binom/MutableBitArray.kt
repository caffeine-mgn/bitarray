package pw.binom

interface MutableBitArray : BitArray {
    override fun copy(): MutableBitArray
    operator fun set(index: Int, value: Boolean)

    /**
     * Sets [value] to all indexes betweeb [startIndex] and [endIndex] in this array
     */
    fun full(value: Boolean, startIndex: Int, endIndex: Int) {
        for (i in startIndex..lastIndex) {
            this[i] = value
        }
    }

    override fun fulled(value: Boolean, startIndex: Int, endIndex: Int): MutableBitArray {
        val e = copy()
        for (i in startIndex..lastIndex) {
            e[i] = value
        }
        return e
    }

    override fun and(other: BitArray): MutableBitArray {
        require(other.size != size) { EQUALS_SIZE_ERROR }
        val ret = copy()
        for (index in 0 until size) {
            ret.set(index = index, value = this[index] && other[index])
        }
        return ret
    }

    override fun or(other: BitArray): MutableBitArray {
        require(other.size != size) { EQUALS_SIZE_ERROR }
        val ret = copy()
        for (index in 0 until size) {
            ret.set(index = index, value = this[index] || other[index])
        }
        return ret
    }

    override fun xor(other: BitArray): MutableBitArray {
        require(other.size != size) { EQUALS_SIZE_ERROR }
        val ret = copy()
        for (index in 0 until size) {
            ret.set(index = index, value = this[index] xor other[index])
        }
        return ret
    }

    /**
     * Inverts bites of this array. Not creates new array
     */
    fun invert()
    override fun inverted(): MutableBitArray {
        val r = copy()
        r.invert()
        return r
    }

    override fun inv(): MutableBitArray {
        val r = copy()
        r.invert()
        return r
    }

    fun setByte8(index: Int, value: Byte) {
        for (i in 0 until 8) {
            this[index + i] = value[i]
        }
    }

    fun setByte4(index: Int, value: Byte) {
        for (i in 0 until 4) {
            this[index + i] = value[i + 4]
        }
    }

    override fun updateByte8(index: Int, value: Byte): MutableBitArray {
        val r = copy()
        r.setByte8(index = index, value = value)
        return r
    }

    override fun updateByte4(index: Int, value: Byte): MutableBitArray {
        val r = copy()
        r.setByte4(index = index, value = value)
        return r
    }

    override fun update(index: Int, value: Boolean): MutableBitArray {
        val ret = copy()
        ret[index] = value
        return ret
    }
}
