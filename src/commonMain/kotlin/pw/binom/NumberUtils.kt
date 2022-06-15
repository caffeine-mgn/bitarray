package pw.binom

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

internal const val EQUALS_SIZE_ERROR = "Size of both BitArray should be equals"
internal inline operator fun Long.get(index: Int) = this and (1L shl (Long.SIZE_BITS - 1 - index)) != 0L
internal inline fun Long.update(index: Int, value: Boolean) =
    if (value)
        (this or (1L shl ((Long.SIZE_BITS - 1 - index))))
    else
        (this.inv() or (1L shl (Long.SIZE_BITS - 1 - index))).inv()

internal inline operator fun Byte.get(index: Int) = this and (1.toByte() shl (Byte.SIZE_BITS - 1 - index)) != 0.toByte()
internal inline fun Byte.update(index: Int, value: Boolean) =
    if (value)
        (this or (1.toByte() shl ((Byte.SIZE_BITS - 1 - index))))
    else
        (this.inv() or (1.toByte() shl (Byte.SIZE_BITS - 1 - index))).inv()

internal infix fun Byte.shl(count: Int) = ((toInt() and 0xFF) shl count).toByte()
internal infix fun Byte.ushr(count: Int) = ((toInt() and 0xFF) ushr count).toByte()
internal fun Byte.Companion.build(
    b1: Boolean,
    b2: Boolean,
    b3: Boolean,
    b4: Boolean,
    b5: Boolean,
    b6: Boolean,
    b7: Boolean,
    b8: Boolean,
) = (
    (if (b1) 0b10000000.toUByte() else 0.toUByte()) +
        (if (b2) 0b01000000.toUByte() else 0.toUByte()) +
        (if (b3) 0b00100000.toUByte() else 0.toUByte()) +
        (if (b4) 0b00010000.toUByte() else 0.toUByte()) +
        (if (b5) 0b00001000.toUByte() else 0.toUByte()) +
        (if (b6) 0b00000100.toUByte() else 0.toUByte()) +
        (if (b7) 0b00000010.toUByte() else 0.toUByte()) +
        (if (b8) 0b00000001.toUByte() else 0.toUByte())
    ).toByte()

internal fun Int.toBitsetString(sb2: StringBuilder = StringBuilder()): String {
    repeat(Int.SIZE_BITS) { index ->
        val mask = 0b1 shl (Int.SIZE_BITS - 1 - index)
        sb2.append(if (this and mask != 0) "1" else "0")
    }
    return sb2.toString()
}

internal fun Long.toBitsetString(sb2: StringBuilder = StringBuilder()): String {
    repeat(Long.SIZE_BITS) { index ->
        val mask = 0b1L shl (Long.SIZE_BITS - 1 - index)
        sb2.append(if (this and mask != 0L) "1" else "0")
    }
    return sb2.toString()
}

internal fun Byte.toBitsetString(sb2: StringBuilder = StringBuilder()): String {
    repeat(Byte.SIZE_BITS) { index ->
        val mask = 0b1.toByte() shl (Byte.SIZE_BITS - 1 - index)
        sb2.append(if (this and mask != 0.toByte()) "1" else "0")
    }
    return sb2.toString()
}
