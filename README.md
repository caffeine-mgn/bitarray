# BitArray

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin 1.6.10](https://img.shields.io/badge/Kotlin-1.6.10-blue.svg?style=flat&logo=kotlin)](http://kotlinlang.org)
[![Gradle build](https://github.com/caffeine-mgn/bitarray/actions/workflows/publish.yml/badge.svg) ](https://github.com/caffeine-mgn/bitarray/actions/workflows/publish.yml) <br><br>

### Support targets
* Jvm
* Linux x64
* Linux Arm32Hfp
* Linux Arm64
* Linux Mips32
* Linux Mipsel32
* Mingw x64
* Mingw x86
* Android Native Arm32
* Android Native Arm64
* Android Native x86
* Android Native x64
* Macos x64
* Macos Arm64
* IOS
* IOS Arm32
* IOS Arm64
* IOS Simulator Arm64
* WatchOS
* WatchOS Arm32
* WatchOS Arm64
* WatchOS Simulator Arm64
* WatchOS x64
* WatchOS x86
* Wasm32
* JS (Legacy and IR)

### Description
BitArray for Kotlin Common Library contains:

* [BitArray32](src/commonMain/kotlin/pw/binom/BitArray32.kt) based in `Int`
* [BitArray64](src/commonMain/kotlin/pw/binom/BitArray64.kt) based on `Long`
* [BytesBitArray](src/commonMain/kotlin/pw/binom/BytesBitArray.kt) based on `ByteArray`
* [LongsBitArray](src/commonMain/kotlin/pw/binom/LongsBitArray.kt) based on `LongArray`

All classes implements [BitArray](src/commonMain/kotlin/pw/binom/BitArray.kt). All classes is `inline` classes.

### Integration
#### Gradle
```kotlin
dependencies {
    implementation("pw.binom:bitarray:0.1.0")
}
```

### Examples

#### BitArray32/BitArray64

```kotlin
var array = BitArray32() // or BitArray64
array = array.update(index = 1, value = true)
println("value is ${array[1]}")
```

#### BytesBitArray/LongsBitArray

```kotlin
val array = BytesBitArray(6) // or LongsBitArray(LongArray(3))
array[1] = true // was modified current value without reassign `array` variable
println("value is ${array[1]}")
val array2 = array.update(index = 1, value = false)// creates new array
println("value is ${array[1]}") // prints "false"
println("value is ${array2[1]}") // prints "true"
```