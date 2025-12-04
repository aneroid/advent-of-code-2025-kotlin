class Day04(input: List<String>) {
    private val data = parseInput(input)
    
    fun partOne(): Int = data.keys.count { pt ->
        pt.neighbours().count { it in data } < 4
    }
    
    fun partTwo(): Int {
        val removeSeq = generateSequence(data.keys to 0) { (points, _) ->
            val remove = points.filter { pt ->
                pt.neighbours().count { it in points } < 4
            }
            points - remove.toSet() to remove.size
        }
        return removeSeq.drop(1).map { it.second }.takeWhile { it != 0 }.sum()
    }
    
    private companion object {
        fun parseInput(input: List<String>) = gridFromLines(input, skipChar = '.', mapper = { it })
    }
}

fun main() {
    val testInput = readInput("Day04_test")
    val input = readInput("Day04")
    
    println("part One:")
    assertThat(Day04(testInput).partOne()).isEqualTo(13)
    println("actual: ${Day04(input).partOne()}\n")
    
    println("part Two:")
    // uncomment when ready
    assertThat(Day04(testInput).partTwo()).isEqualTo(43)
    println("actual: ${Day04(input).partTwo()}\n")
}
