package pw.binom.bitarray

import java.nio.ByteBuffer

internal actual object NativeUtils {
    actual fun byteArrayToLongArray(src: ByteArray): LongArray {
        require(src.size % Long.SIZE_BYTES == 0)
        val output = LongArray(src.size / Long.SIZE_BYTES)
        ByteBuffer.wrap(src).asLongBuffer().get(output)
        return output
    }

    actual fun longArrayToByteArray(src: LongArray): ByteArray {
        val output = ByteArray(src.size * Long.SIZE_BYTES)
        ByteBuffer.wrap(output).asLongBuffer().put(src)
        return output
    }

    actual fun intArrayToByteArray(src: IntArray): ByteArray {
        val output = ByteArray(src.size * Int.SIZE_BYTES)
        ByteBuffer.wrap(output).asIntBuffer().put(src)
        return output
    }

    actual fun longArrayToIntArray(src: LongArray): IntArray {
        TODO()
    }

    actual fun shortArrayToByteArray(src: ShortArray): ByteArray {
        val output = ByteArray(src.size * Int.SIZE_BYTES)
        ByteBuffer.wrap(output).asShortBuffer().put(src)
        return output
    }
}
