package days

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day15 {
    data class SensorDistance(val x: Int, val y: Int, val md: Int)

    fun part1_inefficient(input: List<String>) {
        data class Pt(val x: Int, val y: Int)

        val beacons = mutableSetOf<Pt>()
        val r = "-?\\d+".toRegex()
        val sensors = input.map { l ->
            val (sx, sy, bx, by) = r.findAll(l).toList().map { it.value.toInt() }
            val md = abs(sx - bx) + abs(sy - by)
            beacons.add(Pt(bx, by))
            SensorDistance(sx, sy, md)
        }

        val destinationY = 2_000_000
        val ps = mutableSetOf<Pt>()
        for (s in sensors) {
            (s.y..(s.y + s.md)).forEachIndexed { yIndex, y ->
                if (y == destinationY || (y - (yIndex * 2)) == destinationY) {
                    val currentManhattanDistance = s.md - yIndex
                    (s.x..(s.x + currentManhattanDistance)).forEachIndexed { xIndex, x ->
                        val p1 = Pt(x, y)
                        val p2 = Pt(x - (xIndex * 2), y)
                        val p3 = Pt(x, y - (yIndex * 2))
                        val p4 = Pt(x - (xIndex * 2), y - (yIndex * 2))
                        ps.add(p1)
                        ps.add(p2)
                        ps.add(p3)
                        ps.add(p4)
                    }
                }
            }
        }

        val ans = ps.filter { it.y == destinationY }.filter { !beacons.contains(it) }.count()
        println(ans)
    }

    fun part1(input: List<String>) {
        data class Pt(val x: Int, val y: Int)
        val beacons = mutableSetOf<Pt>()
        val r = "-?\\d+".toRegex()
        val sensors = input.map { l ->
            val (sx, sy, bx, by) = r.findAll(l).toList().map { it.value.toInt() }
            val md = abs(sx - bx) + abs(sy - by)
            beacons.add(Pt(bx, by))
            SensorDistance(sx, sy, md)
        }

        fun isInRange(p: Pt, s: SensorDistance): Boolean {
            return if (abs(s.x - p.x) <= s.md && abs(s.y - p.y) <= s.md) {
                val yd = abs(s.y - p.y)
                val transformation = s.md - yd
                p.x in (s.x - transformation)..(s.x + transformation)
            } else {
                false
            }
        }

        val maxX = sensors.maxOf { it.x +  it.md }
        val res = mutableSetOf<Pt>()
        val y = 2_000_000
        for(x in 0..maxX) {
            sensors.forEach { s ->
               val p = Pt(x, y)
                val ir = isInRange(p, s)
                if(ir && !beacons.contains(p)) {
                    res.add(p)
                }
            }
        }
        println(res.size)

    }

    fun part2(input: List<String>) {
        val r = "-?\\d+".toRegex()
        val sensors = input.map { l ->
            val (sx, sy, bx, by) = r.findAll(l).toList().map { it.value.toInt() }
            val md = abs(sx - bx) + abs(sy - by)
            SensorDistance(sx, sy, md)
        }
        val mv = 4000000
        fun getRange(y: Int, sd: SensorDistance): IntRange? {
            return if (abs(sd.y - y) <= sd.md) {
                val yd = abs(sd.y - y)
                val transformation = sd.md - yd
                max(0, sd.x - transformation) .. min(mv, sd.x + transformation)
            } else {
                null
            }
        }

        (0..mv).forEach { y ->
            val ranges = mutableListOf<IntRange>()
            sensors.forEach { s ->
                getRange(y, s)?.let(ranges::add)
            }
            ranges.sortWith(compareBy(IntRange::first, IntRange::last))
            var prev = ranges[0]
            for (range in ranges) {
                if (range.first > prev.last) {
                    val diff = range.first - prev.last
                    if (diff > 1) {
                        val winX = (range.first + prev.last) / 2
                        val ans = winX * 4000000L + y
                        println("$winX $y $ans")
                        return
                    }
                    prev = range
                } else if (range.last > prev.last) {
                    prev = range
                }
            }
        }
    }
}