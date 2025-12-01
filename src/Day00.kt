class Day00(input: List<String>) {
    private val data = parseInput(input)
    
    fun partOne(): Int {
        println(data)
        return 0
    }
    
    fun partTwo(): Int {
        return 0
    }
    
    private companion object {
        fun parseInput(input: List<String>) = input
    }
}

fun main() {
    val testInput = readInput("Day00_test")
    val input = readInput("Day00")
    
    println("part One:")
    assertThat(Day00(testInput).partOne()).isEqualTo(1)
    println("actual: ${Day00(input).partOne()}\n")
    
    println("part Two:")
    // uncomment when ready
    // assertThat(Day00(testInput).partTwo()).isEqualTo(1)
    // println("actual: ${Day00(input).partTwo()}\n")
}
