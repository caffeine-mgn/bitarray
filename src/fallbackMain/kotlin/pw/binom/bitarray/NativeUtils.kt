package pw.binom.bitarray

import kotlin.experimental.and

internal actual object NativeUtils {
    actual fun byteArrayToLongArray(src: ByteArray): LongArray {
        require(src.size % Long.SIZE_BYTES == 0)
        val ret = LongArray(src.size / Long.SIZE_BYTES)
        var cursorInLong = 0
        while (cursorInLong * Long.SIZE_BYTES < src.size) {
            val cursorInBytes = cursorInLong * Long.SIZE_BYTES
            ret[cursorInLong++] = Long.fromBytes(
                byte0 = src[cursorInBytes + 0],
                byte1 = src[cursorInBytes + 1],
                byte2 = src[cursorInBytes + 2],
                byte3 = src[cursorInBytes + 3],
                byte4 = src[cursorInBytes + 4],
                byte5 = src[cursorInBytes + 5],
                byte6 = src[cursorInBytes + 6],
                byte7 = src[cursorInBytes + 7],
            )
        }
        return ret
    }

    actual fun longArrayToByteArray(src: LongArray): ByteArray {
        val output = ByteArray(src.size * Long.SIZE_BYTES)
        src.forEachIndexed { index, l ->
            var longValue = l
            repeat(Long.SIZE_BYTES) { r ->
                output[index * Long.SIZE_BYTES + (Long.SIZE_BYTES - 1 - r)] = (longValue and 0xFF).toByte()
                longValue = longValue ushr Byte.SIZE_BITS
            }
        }
        return output
    }

    actual fun intArrayToByteArray(src: IntArray): ByteArray {
        val output = ByteArray(src.size * Int.SIZE_BYTES)
        src.forEachIndexed { index, l ->
            var intValue = l
            repeat(Int.SIZE_BYTES) { r ->
                output[index * Int.SIZE_BYTES + (Int.SIZE_BYTES - 1 - r)] = (intValue and 0xFF).toByte()
                intValue = intValue ushr Byte.SIZE_BITS
            }
        }
        return output
    }

    actual fun longArrayToIntArray(src: LongArray): IntArray {
        TODO("Not yet implemented")
    }

    actual fun shortArrayToByteArray(src: ShortArray): ByteArray {
        val output = ByteArray(src.size * Short.SIZE_BYTES)
        src.forEachIndexed { index, l ->
            var intValue = l
            repeat(Short.SIZE_BYTES) { r ->
                output[index * Short.SIZE_BYTES + (Short.SIZE_BYTES - 1 - r)] = (intValue and 0xFF).toByte()
                intValue = intValue ushr Byte.SIZE_BITS
            }
        }
        return output
    }
}
