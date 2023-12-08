package days

object Day14 {

    fun part1(input: List<String>) {
        val (xValues, yValues) = values(input)

        var ans = 0
        fun checkSandRestPos(x: Int, y: Int): Pair<Int, Int> {
            if (y >= xValues[x]?.maxOf { it } ?: 0) return -1 to -1
            return when {
                xValues[x]?.contains(y + 1) != true -> checkSandRestPos(x, y + 1)
                yValues[y + 1]?.contains(x - 1) != true -> checkSandRestPos(x - 1, y)
                yValues[y + 1]?.contains(x + 1) != true -> checkSandRestPos(x + 1, y)
                else -> x to y
            }
        }

        while (true) {
            val (sx, sy) = checkSandRestPos(500, 0)
            if (sx == -1 && sy == -1) break
            else ans++
            xValues.replace(sx, xValues.getOrPut(sx) { setOf() } + sy)
            yValues.replace(sy, yValues.getOrPut(sy) { setOf() } + sx)
        }
        println(ans)
    }

    fun part2(input: List<String>) {
        val (xValues, yValues) = values(input)

        var ans = 0
        fun checkSandRestPos(maxY: Int, x: Int, y: Int): Pair<Int, Int> {
            val rd = y + 1 < maxY
            return when {
                xValues[x]?.contains(y + 1) != true && rd -> checkSandRestPos(maxY, x, y + 1)
                yValues[y + 1]?.contains(x - 1) != true && rd -> checkSandRestPos(maxY,x - 1, y)
                yValues[y + 1]?.contains(x + 1) != true && rd -> checkSandRestPos(maxY,x + 1, y)
                else -> x to y
            }
        }

        val maxValue = xValues.values.flatten().maxOf { it } + 2

        while (true) {
            val (sx, sy) = checkSandRestPos(maxValue, 500, 0)
            ans++
            if (sx == 500 && sy == 0) break
            xValues.replace(sx, xValues.getOrPut(sx) { setOf() } + sy)
            yValues.replace(sy, yValues.getOrPut(sy) { setOf() } + sx)
        }
        println(ans)
    }

    data class Pt(val x: Int, val y: Int)

    private fun create(l: Int, r: Int): List<Int> {
        return (l..l + (r - l)).toList()
    }

    private fun values(input: List<String>): Pair<HashMap<Int, Set<Int>>, HashMap<Int, Set<Int>>> {
        val yValues = HashMap<Int, Set<Int>>()
        val xValues = HashMap<Int, Set<Int>>()
        for (l in input) {
            l.split("->").map {
                it.trim().split(",").map { it.toInt() }.let { (x, y) -> Pt(x, y) }
            }.windowed(2).map { (l, r) ->
                if (l.x == r.x) {
                    if (l.y > r.y) xValues.replace(l.x, xValues.getOrPut(l.x) { setOf() } + create(r.y, l.y))
                    if (l.y < r.y) xValues.replace(l.x, xValues.getOrPut(l.x) { setOf() } + create(l.y, r.y))
                }
                if (l.y == r.y) {
                    if (l.x > r.x) yValues.replace(l.y, yValues.getOrPut(l.y) { setOf() } + create(r.x, l.x))
                    if (l.x < r.x) yValues.replace(l.y, yValues.getOrPut(l.y) { setOf() } + create(l.x, r.x))
                }
            }
        }
        yValues.forEach { (k, v) -> v.forEach { xValues.replace(it, xValues.getOrPut(it) { setOf() } + k) } }
        xValues.forEach { (k, v) -> v.forEach { yValues.replace(it, yValues.getOrPut(it) { setOf() } + k) } }

        return xValues to yValues
    }
}