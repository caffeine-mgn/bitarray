package pw.binom.bitarray
/*
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.posix.memcpy
import pw.binom.bitarray.native.swap_int64_array

internal actual object NativeUtils {
    actual fun byteArrayToLongArray(array: ByteArray): LongArray {
        require(array.size % Long.SIZE_BYTES == 0)
        val output = LongArray(array.size / Long.SIZE_BYTES)

        output.usePinned { outputPinned ->
            array.usePinned { arrayPinned ->
                memcpy(outputPinned.addressOf(0), arrayPinned.addressOf(0), arrayPinned.get().size.convert())
            }
            if (Platform.isLittleEndian) {
                swap_int64_array(outputPinned.addressOf(0), outputPinned.get().size.convert())
            }
        }
        return output
    }

    actual fun longArrayToByteArray(array: LongArray): ByteArray {
        val output = ByteArray(array.size * Long.SIZE_BYTES)

        output.usePinned { outputPinned ->
            array.usePinned { arrayPinned ->
                memcpy(outputPinned.addressOf(0), arrayPinned.addressOf(0), outputPinned.get().size.convert())
            }
            if (Platform.isLittleEndian) {
                swap_int64_array(outputPinned.addressOf(0).reinterpret(), array.size.convert())
            }
        }

        return output
    }
}
*/