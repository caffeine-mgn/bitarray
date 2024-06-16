package pw.binom.bitarray

internal expect object NativeUtils {
    fun byteArrayToLongArray(src: ByteArray): LongArray
    fun longArrayToByteArray(src: LongArray): ByteArray
    fun intArrayToByteArray(src: IntArray): ByteArray
    fun shortArrayToByteArray(src: ShortArray): ByteArray
    fun longArrayToIntArray(src: LongArray): IntArray
}
