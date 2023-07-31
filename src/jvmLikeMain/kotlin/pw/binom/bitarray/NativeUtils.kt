package pw.binom.bitarray

import java.nio.ByteBuffer

internal actual object NativeUtils {
    actual fun byteArrayToLongArray(array: ByteArray): LongArray {
        require(array.size % Long.SIZE_BYTES == 0)
        val output = LongArray(array.size / Long.SIZE_BYTES)
        ByteBuffer.wrap(array).asLongBuffer().get(output)
        return output
    }

    actual fun longArrayToByteArray(array: LongArray): ByteArray {
        val output = ByteArray(array.size * Long.SIZE_BYTES)
        ByteBuffer.wrap(output).asLongBuffer().put(array)
        return output
    }
}
