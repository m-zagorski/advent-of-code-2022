@file:Suppress("DuplicatedCode")

package days

import readLines

object Day23 {
    operator fun invoke() {
        print("Part 1: ")
        part1(readLines("23"))
        print("Part 2 slow: ")
        part2_slow(readLines("23"))
    }

    sealed class Direction {
        abstract val pnts: List<Pt>
        abstract val mp: Pt

        object None : Direction() {
            override val pnts: List<Pt> = listOf(
                Pt(x = 0, y = 0),
                Pt(x = 0, y = 0),
                Pt(x = 0, y = 0)
            )
            override val mp = Pt(x = 0, y = 0)
        }

        object North : Direction() {
            override val pnts: List<Pt> = listOf(
                Pt(x = 0, y = -1),
                Pt(x = 1, y = -1),
                Pt(x = -1, y = -1)
            )
            override val mp = Pt(x = 0, y = -1)
        }

        object South : Direction() {
            override val pnts: List<Pt> = listOf(
                Pt(x = 0, y = 1),
                Pt(x = 1, y = 1),
                Pt(x = -1, y = 1)
            )

            override val mp = Pt(x = 0, y = 1)
        }

        object West : Direction() {
            override val pnts: List<Pt> = listOf(
                Pt(x = -1, y = 0),
                Pt(x = -1, y = 1),
                Pt(x = -1, y = -1)
            )
            override val mp = Pt(x = -1, y = 0)
        }

        object East : Direction() {
            override val pnts: List<Pt> = listOf(
                Pt(x = 1, y = 0),
                Pt(x = 1, y = 1),
                Pt(x = 1, y = -1)
            )
            override val mp = Pt(x = 1, y = 0)
        }
    }

    data class Pt(val x: Int, val y: Int) {
        fun afterMovement(point: Pt): Pt {
            return copy(x = x + point.x, y = y + point.y)
        }

        fun reverse(direction: Direction): Pt {
            return copy(x = x - direction.mp.x, y = y - direction.mp.y)
        }

        fun pointsAfterMovement(pnts: List<Pt>): List<Pt> {
            return pnts.map(::afterMovement)
        }
    }

    data class Prop(val pt: Pt, val direction: Direction)

    fun part1(input: List<String>) {
        var pnts = mutableListOf<Pt>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') pnts.add(Pt(x, y))
            }
        }
        val movements = mutableListOf(Direction.North, Direction.South, Direction.West, Direction.East)

        repeat(10) {
            val propositions = mutableListOf<Prop>()
            for (pt in pnts) {
                val allDirections = movements.map { direction -> pt.pointsAfterMovement(direction.pnts) }.flatten()
                val leave = pnts.none { allDirections.contains(it) }
                if (leave) {
                    propositions.add(Prop(pt, Direction.None))
                    continue
                }

                var canMove = false
                for (direction in movements) {
                    val np = pt.pointsAfterMovement(direction.pnts)
                    val canMoveToDirection = pnts.none { np.contains(it) }
                    if (canMoveToDirection) {
                        canMove = true
                        propositions.add(Prop(pt.afterMovement(direction.mp), direction))
                        break
                    }
                }
                if (!canMove) {
                    propositions.add(Prop(pt, Direction.None))
                }
            }

            movements.add(movements.removeFirst())

            val npnts = mutableListOf<Pt>()
            propositions.groupBy { it.pt }.forEach { (k, v) ->
                if (v.size > 1) {
                    v.forEach { (pt, direction) -> npnts.add(pt.reverse(direction)) }
                } else {
                    npnts.add(k)
                }
            }
            npnts.sortWith(compareBy(Pt::y, Pt::x))
            pnts = npnts
        }

        val maxX = pnts.maxByOrNull { it.x }!!.x
        val minX = pnts.minByOrNull { it.x }!!.x
        val maxY = pnts.maxByOrNull { it.y }!!.y
        val minY = pnts.minByOrNull { it.y }!!.y
        var ans = 0
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (!pnts.contains(Pt(x, y))) ans++
            }
        }
        println(ans)
    }

    fun part2_slow(input: List<String>) {
        var pnts = mutableListOf<Pt>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') pnts.add(Pt(x, y))
            }
        }
        val movements = mutableListOf(Direction.North, Direction.South, Direction.West, Direction.East)

        var i = 1
        while(true) {
            val propositions = mutableListOf<Prop>()
            var elfesMoving = false
            for (pt in pnts) {
                val allDirections = movements.map { direction -> pt.pointsAfterMovement(direction.pnts) }.flatten()
                val leave = pnts.none { allDirections.contains(it) }
                if (leave) {
                    propositions.add(Prop(pt, Direction.None))
                    continue
                }

                var canMove = false
                for (direction in movements) {
                    val np = pt.pointsAfterMovement(direction.pnts)
                    val canMoveToDirection = pnts.none { np.contains(it) }
                    if (canMoveToDirection) {
                        elfesMoving = true
                        canMove = true
                        propositions.add(Prop(pt.afterMovement(direction.mp), direction))
                        break
                    }
                }
                if(!canMove) {
                    propositions.add(Prop(pt, Direction.None))
                }
            }

            if(!elfesMoving) break
            i++

            val first = movements.removeFirst()
            movements.add(first)

            val npnts = mutableListOf<Pt>()
            propositions.groupBy { it.pt }.forEach { (k, v) ->
                if (v.size > 1) {
                    v.forEach { (pt, direction) -> npnts.add(pt.reverse(direction)) }
                } else {
                    npnts.add(k)
                }
            }
            npnts.sortWith(compareBy(Pt::y, Pt::x))
            pnts = npnts

        }

        println(i)
    }
}