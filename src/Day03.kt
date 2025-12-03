class Day03(input: List<String>) {
    private val data = parseInput(input)
    
    fun partOneNaive(): Int {
        return data.sumOf { bank ->
            val bankVals = mutableSetOf<Int>()
            for (idx in bank.indices) {
                val left = bank[idx]
                bank.drop(idx + 1).forEach { right -> bankVals.add("$left$right".toInt()) }
            }
            bankVals.max()
        }
    }
    
    fun maxInOrder(bank: String, len: Int): String {
        // println("bank: $bank, len: $len")
        val indexedBank = bank.withIndex()
        var nextStart = 0
        val lefts = buildList<Char> {
            for (i in 1..len) {
                val max = indexedBank
                    .drop(nextStart)
                    .dropLast(len - i)
                    .maxBy { it.value }
                add(max.value)
                nextStart = max.index + 1
                
                // println("  $this")
                // println("  nextStart: $nextStart")
                // println("  remaining: ${indexedBank.drop(nextStart).joinToString("") { it.value.toString() }}")
            }
        }
        return lefts.joinToString("")
    }
    
    fun partOne(): Int = data.sumOf { maxInOrder(it, 2).toInt() }
    
    fun partTwo(): Long = data.sumOf { maxInOrder(it, 12).toLong() }
    
    private companion object {
        fun parseInput(input: List<String>) = input
    }
}

fun main() {
    val testInput = readInput("Day03_test")
    val input = readInput("Day03")
    
    println("part One:")
    assertThat(Day03(testInput).partOneNaive()).isEqualTo(357)
    println("actual: ${Day03(input).partOneNaive()}\n")
    
    val testDay = Day03(emptyList())
    assertThat(testDay.maxInOrder("123", 1)).isEqualTo("3")
    assertThat(testDay.maxInOrder("123", 2)).isEqualTo("23")
    assertThat(testDay.maxInOrder("321", 2)).isEqualTo("32")
    assertThat(testDay.maxInOrder("1231", 3)).isEqualTo("231")
    assertThat(testDay.maxInOrder("2131", 3)).isEqualTo("231")
    
    assertThat(Day03(testInput).partOne()).isEqualTo(357)
    println("actual: ${Day03(input).partOne()}\n")
    
    println("part Two:")
    // uncomment when ready
    assertThat(Day03(testInput).partTwo()).isEqualTo(3121910778619)
    println("actual: ${Day03(input).partTwo()}\n")
}
