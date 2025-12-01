class Day01(input: List<String>) {
    private val data = parseInput(input)
    private val initialPosition = 50
    private val totalPositions = 100
    private val dirSign = mapOf('L' to -1, 'R' to 1)
    
    fun partOne(): Int =
        data
            .runningFold(initialPosition) { position, (dir, clicks) ->
                val sign = dirSign.getValue(dir)
                (position + clicks * sign).mod(totalPositions)
            }
            .count { it == 0 }
    
    fun partTwo(): Int {
        var zeroes = 0
        data
            .fold(initialPosition) { position, (dir, clicks) ->
                val sign = dirSign.getValue(dir)
                val positions = generateSequence(position) {
                    (it + 1 * sign).mod(totalPositions)
                }.drop(1).take(clicks).toList()
                println("positions: $positions")
                zeroes += positions.count { it == 0 }
                println("zeroes: $zeroes")
                positions.last().aprintln()
            }
        
        return zeroes
    }
    
    private companion object {
        fun parseInput(input: List<String>) = input.map { it.first() to it.drop(1).toInt() }
    }
}

fun main() {
    val testInput = readInput("Day01_test")
    val input = readInput("Day01")
    
    println("part One:")
    assertThat(Day01(testInput).partOne()).isEqualTo(3)
    println("actual: ${Day01(input).partOne()}\n")
    
    println("part Two:")
    // uncomment when ready
    assertThat(Day01(testInput).partTwo()).isEqualTo(6)
    println("actual: ${Day01(input).partTwo()}\n")
}
