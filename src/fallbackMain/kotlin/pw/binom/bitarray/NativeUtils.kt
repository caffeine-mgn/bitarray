package pw.binom.bitarray

internal actual object NativeUtils {
    actual fun byteArrayToLongArray(array: ByteArray): LongArray {
        require(array.size % Long.SIZE_BYTES == 0)
        val ret = LongArray(array.size / Long.SIZE_BYTES)
        var cursorInLong = 0
        while (cursorInLong * Long.SIZE_BYTES < array.size) {
            val cursorInBytes = cursorInLong * Long.SIZE_BYTES
            ret[cursorInLong++] = Long.fromBytes(
                byte0 = array[cursorInBytes + 0],
                byte1 = array[cursorInBytes + 1],
                byte2 = array[cursorInBytes + 2],
                byte3 = array[cursorInBytes + 3],
                byte4 = array[cursorInBytes + 4],
                byte5 = array[cursorInBytes + 5],
                byte6 = array[cursorInBytes + 6],
                byte7 = array[cursorInBytes + 7],
            )
        }
        return ret
    }

    actual fun longArrayToByteArray(array: LongArray): ByteArray {
        val output = ByteArray(array.size * Long.SIZE_BYTES)
        array.forEachIndexed { index, l ->
            var longValue = l
            repeat(Long.SIZE_BYTES) { r ->
                output[index * Long.SIZE_BYTES + (7 - r)] = (longValue and 0xFF).toByte()
                longValue = longValue ushr 8
            }
        }
        return output
    }
}
