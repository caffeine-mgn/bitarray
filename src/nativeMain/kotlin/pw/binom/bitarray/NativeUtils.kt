package pw.binom.bitarray

import kotlinx.cinterop.*
import pw.binom.bitarray.native.*
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
internal actual object NativeUtils {
    actual fun byteArrayToLongArray(array: ByteArray): LongArray {
        require(array.size % Long.SIZE_BYTES == 0)
        val output = LongArray(array.size / Long.SIZE_BYTES)

        output.usePinned { outputPinned ->
            array.usePinned { arrayPinned ->
                internal_byteArrayToLongArray(arrayPinned.addressOf(0),outputPinned.addressOf(0),arrayPinned.get().size.convert())
            }
        }
        return output
    }

    actual fun longArrayToByteArray(array: LongArray): ByteArray {
        val output = ByteArray(array.size * Long.SIZE_BYTES)
        output.usePinned { outputPinned ->
            array.usePinned { arrayPinned ->
                internal_byteArrayToLongArray(arrayPinned.addressOf(0),outputPinned.addressOf(0),output.size.convert())
            }
        }
        return output
    }
}
