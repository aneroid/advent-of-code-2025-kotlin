class Day04(input: List<String>) {
    private val data = parseInput(input)
    
    fun partOne(): Int = data.count { (pt, _) ->
        pt.neighbours().count { data[it] != null } < 4
    }
    
    fun partTwo(): Int {
        val removeSeq = generateSequence(emptySet<Point>()) { removedPoints ->
            // println("removedPoints: $removedPoints")
            val remove = data.filter { (pt, _) ->
                pt !in removedPoints && pt.neighbours().count { it !in removedPoints && data[it] != null } < 4
            }
            // println(remove)
            if (remove.isEmpty()) {
                null
            } else {
                removedPoints + remove.keys
            }
        }
        return removeSeq.map { it.size }.last()
    }
    
    private companion object {
        fun parseInput(input: List<String>) = gridFromLines(input, skipChar = '.', mapper = { c: Char -> c })
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
