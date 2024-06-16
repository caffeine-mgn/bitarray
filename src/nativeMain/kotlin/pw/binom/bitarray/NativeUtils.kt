package pw.binom.bitarray

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import pw.binom.bitarray.native.internal_copy_bytes
import pw.binom.bitarray.native.internal_swap_int
import pw.binom.bitarray.native.internal_swap_long
import pw.binom.bitarray.native.internal_swap_short
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
internal actual object NativeUtils {
    actual fun byteArrayToLongArray(src: ByteArray): LongArray {
        require(src.size % Long.SIZE_BYTES == 0)
        val sizeInBytes = src.size
        val sizeInTarget = sizeInBytes / Long.SIZE_BYTES
        val dest = LongArray(sizeInTarget)

        dest.usePinned { destPinned ->
            src.usePinned { srcPinned ->
                internal_copy_bytes(
                    src = srcPinned.addressOf(0),
                    dest = destPinned.addressOf(0),
                    sizeInBytes = sizeInBytes,
                )
                internal_swap_long(
                    longArray = destPinned.addressOf(0),
                    size = sizeInTarget,
                )
            }
        }
        return dest
    }

    actual fun longArrayToByteArray(src: LongArray): ByteArray {
        val sizeInBytes = src.size * Long.SIZE_BYTES
        val sizeInSource = src.size
        val dest = ByteArray(sizeInBytes)
        dest.usePinned { destPinned ->
            src.usePinned { srcPinned ->
                internal_copy_bytes(
                    src = srcPinned.addressOf(0),
                    dest = destPinned.addressOf(0),
                    sizeInBytes = sizeInBytes,
                )
                internal_swap_long(
                    longArray = destPinned.addressOf(0),
                    size = sizeInSource,
                )
            }
        }
        return dest
    }

    actual fun intArrayToByteArray(src: IntArray): ByteArray {
        val sizeInSrc = src.size
        val sizeInBytes = src.size * Int.SIZE_BYTES
        val dest = ByteArray(sizeInBytes)
        dest.usePinned { destPinned ->
            src.usePinned { srcPinned ->
                internal_copy_bytes(
                    src = srcPinned.addressOf(0),
                    dest = destPinned.addressOf(0),
                    sizeInBytes = sizeInBytes,
                )
                internal_swap_int(
                    intArray = destPinned.addressOf(0),
                    size = sizeInSrc,
                )
            }
        }
        return dest
    }

    actual fun longArrayToIntArray(src: LongArray): IntArray {
        val sizeInSrc = src.size
        val sizeInDest = sizeInSrc * 2
        val sizeInBytes = sizeInSrc * Long.SIZE_BYTES
        val output = IntArray(sizeInDest)
        output.usePinned { outputPinned ->
            src.usePinned { arrayPinned ->
                internal_copy_bytes(
                    src = arrayPinned.addressOf(0),
                    dest = outputPinned.addressOf(0),
                    sizeInBytes = sizeInBytes,
                )
                internal_swap_long(
                    longArray = outputPinned.addressOf(0),
                    size = sizeInSrc,
                )
                internal_swap_int(
                    intArray = outputPinned.addressOf(0),
                    size = sizeInDest,
                )
            }
        }
        return output
    }

    actual fun shortArrayToByteArray(src: ShortArray): ByteArray {
        val sizeInSrc = src.size
        val sizeInBytes = src.size * Short.SIZE_BYTES
        val dest = ByteArray(sizeInBytes)
        dest.usePinned { destPinned ->
            src.usePinned { srcPinned ->
                internal_copy_bytes(
                    src = srcPinned.addressOf(0),
                    dest = destPinned.addressOf(0),
                    sizeInBytes = sizeInBytes,
                )
                internal_swap_short(
                    shortArray = destPinned.addressOf(0),
                    size = sizeInSrc,
                )
            }
        }
        return dest
    }
}
