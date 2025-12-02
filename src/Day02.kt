class Day02(input: String) {
    private val data = parseInput(input)
    
    private fun longsInRange(r: Pair<Long, Long>) = LongProgression.fromClosedRange(r.first, r.second, 1L)
    
    private fun isInvalidPartOne(num: Long): Boolean {
        val strNum = num.toString()
        val length = strNum.length
        return when {
            length % 2 != 0 -> false
            else -> strNum.take(length / 2) == strNum.takeLast(length / 2)
        }
    }
    
    private fun isInvalidPartTwo(num: Long) = Regex("""(\d+)\1+""").matches(num.toString())
    
    fun partOne(): Long =
        data
            .asSequence()
            .flatMap(::longsInRange)
            .filter(::isInvalidPartOne)
            .sum()
    
    fun partTwo(): Long =
        data
            .asSequence()
            .flatMap(::longsInRange)
            .filter(::isInvalidPartTwo)
            .sum()
    
    private companion object {
        fun parseInput(input: String) =
            input.trim()
                .split(",")
                .map { it.split("-") }
                .map { it.first().toLong() to it.last().toLong() }
    }
}

fun main() {
    val testInput = readInputAsString("Day02_test")
    val input = readInputAsString("Day02")
    
    println("part One:")
    assertThat(Day02(testInput).partOne()).isEqualTo(1227775554)
    println("actual: ${Day02(input).partOne()}\n")
    
    println("part Two:")
    // uncomment when ready
    assertThat(Day02(testInput).partTwo()).isEqualTo(4174379265)
    println("actual: ${Day02(input).partTwo()}\n")
}
