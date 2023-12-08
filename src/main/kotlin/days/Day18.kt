package days

object Day18 {
    fun part1(input: List<String>) {
        data class Point(val x: Int, val y: Int, val z: Int) {
            fun neightboors(): Set<Point> {
                return setOf(
                    copy(x = x + 1),
                    copy(x = x - 1),
                    copy(y = y + 1),
                    copy(y = y - 1),
                    copy(z = z + 1),
                    copy(z = z - 1)
                )
            }
        }

        val p = input.map { l ->
            val (x, y, z) = l.split(",").map { it.toInt() }
            Point(x, y, z)
        }
        val ans = p.sumOf { point ->
            6 - point.neightboors().count { n -> n in p }
        }
        println(ans)
    }
}