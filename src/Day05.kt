class Day05(input: List<List<String>>) {
    private val data = parseInput(input)
    private val freshRanges = data.first
    private val availableIDs = data.second
    
    fun LongRange.hasOverlapWith(other: LongRange) = first <= other.last && last >= other.first
    
    fun LongRange.getOverlapUnionWith(other: LongRange): LongRange =
        listOf(first, other.first).min()..listOf(last, other.last).max()
    
    private tailrec fun LongRange.addToNormalised(normalisedRanges: MutableList<LongRange>) {
        // println("\ndoing: $this")
        // println("normalised: $normalisedRanges")
        for ((idx, normRange) in normalisedRanges.withIndex()) {
            if (!this.hasOverlapWith(normRange)) {
                continue
            }
            if (this == normRange) {
                return
            }
            val newRange = this.getOverlapUnionWith(normRange)
            // println("newRange: $newRange")
            if (newRange == normRange) {
                return
            }
            normalisedRanges.removeAt(idx)
            return newRange.addToNormalised(normalisedRanges)
        }
        // println("adding: $this")
        normalisedRanges.add(this)
    }
    
    fun partOne(): Int =
        availableIDs.filter { ing ->
            freshRanges.any { ing in it }
        }.toSet().size
    
    fun partTwo(): Long {
        val normalisedRanges: MutableList<LongRange> = mutableListOf()
        freshRanges.forEach { freshRange ->
            freshRange.addToNormalised(normalisedRanges)
        }
        return normalisedRanges.sumOf { it.last - it.first + 1 }
    }
    
    fun checkOverlap() {
        assertThat((1L..3).hasOverlapWith(2L..4)).isEqualTo(true)
        assertThat((2L..4).hasOverlapWith(1L..3)).isEqualTo(true)
        assertThat((1L..3).hasOverlapWith(4L..4)).isEqualTo(false)
        assertThat((4L..4).hasOverlapWith(1L..3)).isEqualTo(false)
        assertThat((1L..5).hasOverlapWith(2L..4)).isEqualTo(true)
        assertThat((2L..4).hasOverlapWith(1L..5)).isEqualTo(true)
        
    }
    
    private companion object {
        fun parseInput(input: List<List<String>>) = input.first().map {
            it.substringBefore("-").toLong()..it.substringAfter("-").toLong()
        } to input.last().map(String::toLong)
        
    }
}

fun main() {
    val testInput = readInputBlocks("Day05_test")
    val input = readInputBlocks("Day05")
    
    println("part One:")
    assertThat(Day05(testInput).partOne()).isEqualTo(3)
    println("actual: ${Day05(input).partOne()}\n")
    
    println("part Two:")
    val testDay = Day05(testInput)
    testDay.checkOverlap()
    
    assertThat(Day05(testInput).partTwo()).isEqualTo(14)
    println("actual: ${Day05(input).partTwo()}\n")
}
