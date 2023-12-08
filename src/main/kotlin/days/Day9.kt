package days

import kotlin.math.abs

object Day9 {

    fun part1(input: List<String>) {
        data class Pos(val x: Int = 0, val y: Int = 0)

        var h = Pos()
        var t = Pos()
        val v = mutableSetOf(t)

        fun gv(diagonal: Boolean, v: Int): Int {
            val n = if (v == 0) 0 else if (v < 0) v + 1 else v - 1
            return if (diagonal && n == 0) v else n
        }

        for (l in input) {
            val (i, s) = l.split(" ")
            for (pos in 1..s.toInt()) {
                when (i) {
                    "R" -> h = h.copy(x = h.x + 1, y = h.y)
                    "L" -> h = h.copy(x = h.x - 1, y = h.y)
                    "U" -> h = h.copy(x = h.x, y = h.y - 1)
                    "D" -> h = h.copy(x = h.x, y = h.y + 1)
                }
                val xd = (h.x - t.x)
                val yd = (h.y - t.y)
                val isDiagonal = abs(xd) + abs(yd) == 3
                t = t.copy(
                    x = t.x + gv(isDiagonal, xd),
                    y = t.y + gv(isDiagonal, yd)
                )
                v.add(t)
            }
        }
        println(v.size)
    }

    fun part2(input: List<String>) {
        data class Pos(val x: Int = 0, val y: Int = 0)

        var t = (0..9).map { Pos() }
        val v = mutableSetOf(Pos())

        fun gv(diagonal: Boolean, v: Int): Int {
            val n = if (v == 0) 0 else if (v < 0) v + 1 else v - 1
            return if (diagonal && n == 0) v else n
        }

        for (l in input) {
            val (i, s) = l.split(" ")
            for (pos in 1..s.toInt()) {
                val tmpT = mutableListOf<Pos>()
                for ((index, cv) in t.withIndex()) {
                    if (index == 0) {
                        when (i) {
                            "R" -> tmpT.add(cv.copy(x = cv.x + 1, y = cv.y))
                            "L" -> tmpT.add(cv.copy(x = cv.x - 1, y = cv.y))
                            "U" -> tmpT.add(cv.copy(x = cv.x, y = cv.y - 1))
                            "D" -> tmpT.add(cv.copy(x = cv.x, y = cv.y + 1))
                        }
                        continue
                    }
                    val previous = tmpT[index - 1]
                    val xd = (previous.x - cv.x)
                    val yd = (previous.y - cv.y)
                    val isDiagonal = abs(xd) + abs(yd) == 3
                    val new = cv.copy(
                        x = cv.x + gv(isDiagonal, xd),
                        y = cv.y + gv(isDiagonal, yd)
                    )
                    tmpT.add(new)
                    if (index == 9) {
                        v.add(new)
                    }
                }
                t = tmpT
            }
        }
        println(v.size)
    }
}