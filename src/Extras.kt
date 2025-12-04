import kotlin.math.absoluteValue
import kotlin.math.sign

data class Point(val x: Int, val y: Int) {
    constructor(strCoords: String) : this(
        x = strCoords.split(',').first().toInt(),
        y = strCoords.split(',').last().toInt()
    )
    
    constructor(pairCoords: Pair<Int, Int>) : this(x = pairCoords.first, y = pairCoords.second)
    
    override fun toString() = "Pt($x, $y)"
    
    fun toPair() = Pair(x, y)
    
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    
    operator fun rem(other: Point) = Point(x % other.x, y % other.y)
    
    fun mod(other: Point) = Point(x.mod(other.x), y.mod(other.y))
    
    operator fun times(other: Int) = Point(x * other, y * other)
    
    fun manDist(other: Point): Int = (x - other.x).absoluteValue + (y - other.y).absoluteValue
    
    /**
     * Path to another point which is either horizontal, vertical or diagonal (Square)
     */
    fun squareRangeTo(other: Point) = when {
        x == other.x -> (minOf(y, other.y)..maxOf(y, other.y)).map { Point(x, it) }
        y == other.y -> (minOf(x, other.x)..maxOf(x, other.x)).map { Point(it, y) }
        else -> {
            // can be used for all conditions, including the part above
            val xStep = (other.x - x).sign
            val yStep = (other.y - y).sign
            generateSequence(this) { point ->
                if (point == other)
                    null
                else
                    Point(point.x + xStep, point.y + yStep)
            }.toList()
        }
    }
    
    operator fun rangeTo(other: Point): List<Point> {
        val xRange = minOf(x, other.x)..maxOf(x, other.x)
        val yRange = minOf(y, other.y)..maxOf(y, other.y)
        return xRange.flatMap { col -> yRange.map { row -> Point(col, row) } }
    }
    
    /**
     * Find neighbours in a Wider surrounding
     */
    fun neighbours(xRange: IntRange, yRange: IntRange): List<Point> =
        yRange.flatMap { yOff ->
            xRange.mapNotNull { xOff ->
                Point(x + xOff, y + yOff)
                    .takeUnless { xOff == 0 && yOff == 0 }
            }
        }
    
    /**
     * Immediate neighbours
     */
    fun neighbours(): List<Point> =
        listOf(
            // @formatter:off
            Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1),
            Point(x - 1, y),                      Point(x + 1, y),
            Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1),
            // @formatter:on
        )
    
    fun neighboursUDLR(): List<Point> =
        listOf(
            // @formatter:off
                             Point(x, y - 1),
            Point(x - 1, y),                  Point(x + 1, y),
                             Point(x, y + 1),
            // @formatter:on
        )
    
    private fun Point.neighboursWithDir() =
        listOf(
            // @formatter:off
                                            Point(x, y - 1) to Direction.N,
            Point(x - 1, y) to Direction.W,                                 Point(x + 1, y) to Direction.E,
                                            Point(x, y + 1) to Direction.S,
            // @formatter:on
        )
    
    fun neighboursDiags(): List<Point> =
        listOf(
            // @formatter:off
            Point(x - 1, y - 1),             Point(x + 1, y - 1),
                               // Point(0, 0)
            Point(x - 1, y + 1),             Point(x + 1, y + 1),
            // @formatter:on
        )
    
    companion object {
        val ORIGIN = Point(0, 0)
    }
}

operator fun Pair<IntRange, IntRange>.contains(point: Point) = point.x in first && point.y in second

enum class Direction(val offset: Point) {
    N(Point(0, -1)), E(Point(1, 0)), W(Point(-1, 0)), S(Point(0, 1)),
    NE(Point(1, -1)), SE(Point(1, 1)), SW(Point(-1, 1)), NW(Point(-1, -1));
    
    fun toChar() = when (this) {
        N -> '^'
        E -> '>'
        W -> '<'
        S -> 'v'
        else -> throw IllegalArgumentException("Invalid direction: $this")
    }
    
    fun toUniChar() = when (this) {
        N -> '↑'
        E -> '→'
        W -> '←'
        S -> '↓'
        NE -> '↗'
        SE -> '↘'
        SW -> '↙'
        NW -> '↖'
    }
    
    fun opposite() = when (this) {
        N -> S
        E -> W
        S -> N
        W -> E
        else -> throw IllegalArgumentException("Invalid direction: $this")
    }
    
    fun turnRight() = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
        else -> throw IllegalArgumentException("Invalid direction: $this")
    }
    
    companion object {
        fun fromChar(ch: Char) = when (ch) {
            '^' -> N
            '>' -> E
            '<' -> W
            'v' -> S
            else -> throw IllegalArgumentException("Invalid direction char: $ch")
        }
        
        fun fromOffset(point: Point) = Direction.entries.first { it.offset == point }
    }
}

typealias Grid<T> = Map<Point, T>

fun <T> gridFromLines(input: List<String>, skipChar: Char? = null, mapper: (Char) -> T): Grid<T> =
    // mapper can default to `{ it }`
    buildMap {
        input.flatMapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, ch ->
                if (ch != skipChar)
                    this[Point(colIdx, rowIdx)] = mapper(ch)
            }
        }
    }

fun <T> Grid<T>.pretty() =
    (0..keys.maxOf { it.y }).joinToString("\n") { row ->
        (0..keys.maxOf { it.x }).joinToString("") { col ->
            get(Point(col, row)).toString()
        }
    }

fun <T> charLocationsFromLines(input: List<String>, skipChar: Char? = '.', mapper: (Char) -> T): Map<T, List<Point>> =
    buildMap {
        input.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, ch ->
                if (skipChar == null || ch != skipChar)
                    this[mapper(ch)] = getOrPut(mapper(ch), ::listOf) + Point(colIdx, rowIdx)
            }
        }
    }

fun <T> Grid<T>.charLocations(): Map<T, List<Point>> =
    buildMap {
        this@charLocations.forEach { (pt, ch) ->
            this[ch] = getOrDefault(ch, emptyList()) + pt
        }
    }

fun Set<Point>.pretty(rows: Int, cols: Int, noChar: Char = '.', yesFunc: (Point) -> Char = { '#' }) =
    (0..<rows).joinToString("\n") { row ->
        (0..<cols).joinToString("") { col ->
            val pt = Point(col, row)
            (if (pt in this) yesFunc(pt) else noChar).toString()
        }
    }

fun List<String>.transpose(): List<String> =
    buildList {
        for (idx in this@transpose[0].indices) {
            add(this@transpose.joinToString("") { it[idx].toString() })
        }
    }

/**
 * Solve for Integer coefficients
 *
 * Given two lines going through the origin and these, solve for M & N
 * such that axM + bxN = px & ayM + byN = py
 *
 */
fun solveLinear(a: Pair<Long, Long>, b: Pair<Long, Long>, p: Pair<Long, Long>): Pair<Long, Long> {
    val (ax, ay) = a
    val (bx, by) = b
    val (px, py) = p
    val m = (px * by - py * bx) / (ax * by - ay * bx)
    val n = (px - ax * m) / bx
    // make sure 'm' and 'n' don't have rounding errors
    val isSafeCoefficients = m * ax + n * bx == px && m * ay + n * by == py
    return if (isSafeCoefficients) Pair(m, n) else Pair(0, 0)
}
