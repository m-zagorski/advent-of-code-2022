@file:Suppress("DuplicatedCode")

package days

import readLines

object Day24 {

    operator fun invoke() {
        print("Part 1: ")
        part1(readLines("24"))
        print("Part 2: ")
        part2(readLines("24"))
    }

    data class Pt(val x: Int, val y: Int, val dist: Int = 0)
    data class Storm(var pos: Pt, val direction: Pt, val xBound: Int, val yBound: Int, val repr: Char) {
        fun move(): Storm {
            var nx = pos.x + direction.x
            var ny = pos.y + direction.y

            if (nx == 0) nx = xBound - 1
            if (nx == xBound) nx = 1
            if (ny == 0) ny = yBound - 1
            if (ny == yBound) ny = 1

            return copy(pos = Pt(x = nx, y = ny))
        }

        companion object {
            fun create(c: Char, x: Int, y: Int, xBound: Int, yBound: Int): Storm? {
                return when (c) {
                    '<' -> Storm(Pt(x, y), Pt(x = -1, y = 0), xBound, yBound, c)
                    '>' -> Storm(Pt(x, y), Pt(x = 1, y = 0), xBound, yBound, c)
                    '^' -> Storm(Pt(x, y), Pt(x = 0, y = -1), xBound, yBound, c)
                    'v' -> Storm(Pt(x, y), Pt(x = 0, y = 1), xBound, yBound, c)
                    else -> null
                }
            }
        }
    }

    fun part1(input: List<String>) {
        val yBound = input.size - 1
        val xBound = input.first().length - 1
        var sp = Pt(0, 0)
        var ep = Pt(0, 0)
        val storms = mutableListOf<Storm>()

        input.forEachIndexed { y, s ->
            when (y) {
                0 -> sp = Pt(s.asIterable().indexOf('.'), y)
                yBound -> ep = Pt(s.asIterable().indexOf('.'), y)
                else -> {
                    s.forEachIndexed { x, c ->
                        Storm.create(c, x, y, xBound, yBound)?.let(storms::add)
                    }
                }
            }
        }

        fun printBoard(storms: List<Storm>, xBound: Int, yBound: Int, cx: Int, cy: Int) {
            for (y in 0..yBound) {
                for (x in 0..xBound) {
                    if (cx == x && cy == y) {
                        print('E')
                        continue
                    }
                    if ((sp.x == x && sp.y == y) || ep == Pt(x, y)) {
                        print('.')
                        continue
                    }
                    if (x == 0 || y == 0 || x == xBound || y == yBound) {
                        print('#')
                        continue
                    }
                    storms.firstOrNull { it.pos == Pt(x, y) }?.let { print(it.repr) } ?: print('.')
                }
                println()
            }
        }

        val queue = mutableListOf<Pt>()
        queue.add(sp)

        fun isValid(x: Int, y: Int, storms: Set<Pt>, destination: Pt): Boolean {
            val pt = Pt(x, y)
            return destination == pt || (pt.x > 0 && pt.y > 0 && pt.x < xBound && pt.y < yBound && pt !in storms)
        }

        data class StormCache(val storm: List<Storm>, val pts: Set<Pt>)
        val cachedStorms: HashMap<Int, StormCache> = hashMapOf(0 to StormCache(storms, storms.map { it.pos }.toSet()))
        fun getStormOrCreate(minute: Int): Set<Pt> {
            return cachedStorms.computeIfAbsent(minute) { key ->
                val ns = cachedStorms.getValue(key - 1).storm.map { storm -> storm.move() }
                StormCache(ns, ns.map { it.pos }.toSet())
            }.pts
        }

        val alreadyChecked = mutableSetOf<Pt>()
        while (queue.isNotEmpty()) {
            val item = queue.removeFirst()
            val (cx, cy, distance) = item

            if(alreadyChecked.contains(item)) continue
            alreadyChecked.add(item)

            if (cx == ep.x && cy == ep.y) {
                println(distance)
                break
            }

            val localStorms = getStormOrCreate(distance+1)

            if(Pt(cx, cy) !in localStorms) {
                queue.add(Pt(cx, cy, distance + 1))
            }

            if (isValid(cx, cy - 1, localStorms, ep)) {
                queue.add(Pt(cx, cy - 1, distance + 1))
            }

            if (isValid(cx, cy + 1, localStorms, ep)) {
                queue.add(Pt(cx, cy + 1, distance + 1))
            }

            if (isValid(cx - 1, cy, localStorms, ep)) {
                queue.add(Pt(cx - 1, cy, distance + 1))
            }

            if (isValid(cx + 1, cy, localStorms, ep)) {
                queue.add(Pt(cx + 1, cy, distance + 1))
            }
        }
    }

    fun part2(input: List<String>) {
        val yBound = input.size - 1
        val xBound = input.first().length - 1
        var sp = Pt(0, 0)
        var ep = Pt(0, 0)
        val storms = mutableListOf<Storm>()

        input.forEachIndexed { y, s ->
            when (y) {
                0 -> sp = Pt(s.asIterable().indexOf('.'), y)
                yBound -> ep = Pt(s.asIterable().indexOf('.'), y)
                else -> {
                    s.forEachIndexed { x, c ->
                        Storm.create(c, x, y, xBound, yBound)?.let(storms::add)
                    }
                }
            }
        }

        fun calculate(sp: Pt, ep: Pt, storms: List<Storm>): Pair<Int, List<Storm>> {
            val queue = mutableListOf<Pt>()
            queue.add(sp)
            fun isValid(x: Int, y: Int, storms: Set<Pt>, destination: Pt): Boolean {
                val pt = Pt(x, y)
                return destination == pt || (pt.x > 0 && pt.y > 0 && pt.x < xBound && pt.y < yBound && pt !in storms)
            }

            data class StormCache(val storm: List<Storm>, val pts: Set<Pt>)
            val cachedStorms: HashMap<Int, StormCache> = hashMapOf(0 to StormCache(storms, storms.map { it.pos }.toSet()))
            fun getStormOrCreate(minute: Int): StormCache {
                return cachedStorms.computeIfAbsent(minute) { key ->
                    val ns = cachedStorms.getValue(key - 1).storm.map { storm -> storm.move() }
                    StormCache(ns, ns.map { it.pos }.toSet())
                }
            }

            val alreadyChecked = mutableSetOf<Pt>()
            while (queue.isNotEmpty()) {
                val item = queue.removeFirst()
                val (cx, cy, distance) = item

                if (alreadyChecked.contains(item)) continue
                alreadyChecked.add(item)

                if (cx == ep.x && cy == ep.y) {
                    return distance to getStormOrCreate(distance).storm
                }

                val localStorms = getStormOrCreate(distance + 1).pts

                if (Pt(cx, cy) !in localStorms) {
                    queue.add(Pt(cx, cy, distance + 1))
                }

                if (isValid(cx, cy - 1, localStorms, ep)) {
                    queue.add(Pt(cx, cy - 1, distance + 1))
                }

                if (isValid(cx, cy + 1, localStorms, ep)) {
                    queue.add(Pt(cx, cy + 1, distance + 1))
                }

                if (isValid(cx - 1, cy, localStorms, ep)) {
                    queue.add(Pt(cx - 1, cy, distance + 1))
                }

                if (isValid(cx + 1, cy, localStorms, ep)) {
                    queue.add(Pt(cx + 1, cy, distance + 1))
                }
            }
            return -1 to emptyList()
        }

        val (d1, s1) = calculate(sp, ep, storms)
        val (d2, s2) = calculate(ep, sp, s1)
        val (d3, _) = calculate(sp, ep, s2)
        val ans = d1 + d2 + d3
        println(ans)
    }
}