// via ephemient
// https://github.com/ephemient/aoc2023/blob/main/kt/aoc2023-lib/src/commonMain/kotlin/com/github/ephemient/aoc2023/Math.kt

fun gcd(x: Long, y: Long): Long {
    var a = x
    var b = y
    while (b != 0L) a = b.also { b = a.mod(b) }
    return a
}

fun lcm(x: Long, y: Long): Long = x / gcd(x, y) * y
