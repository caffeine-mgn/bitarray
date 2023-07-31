package pw.binom.bitarray

internal expect object NativeUtils {
    fun byteArrayToLongArray(array: ByteArray): LongArray
    fun longArrayToByteArray(array: LongArray): ByteArray
}
