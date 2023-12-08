package days

import readFile


object Day22 {

    operator fun invoke() {
        print("Part 1: ")
        part1(readFile("22"))
        print("Part 2: ")
        part2(readFile("22"))
    }

    sealed class Tile {
        abstract val x: Int
        abstract val y: Int

        data class Empty(override val x: Int, override val y: Int) : Tile()
        data class Wall(override val x: Int, override val y: Int) : Tile()

        fun canMove(): Boolean = this is Empty
    }

    data class Pt(val x: Int, val y: Int)
    sealed class Direction {
        object Up : Direction()
        object Down : Direction()
        object Left : Direction()
        object Right : Direction()

        fun facingValue(): Int {
            return when (this) {
                Down -> 1
                Left -> 2
                Right -> 0
                Up -> 3
            }
        }

        fun move(pt: Pt): Pt {
            return when (this) {
                Down -> Pt(pt.x, pt.y + 1)
                Left -> Pt(pt.x - 1, pt.y)
                Right -> Pt(pt.x + 1, pt.y)
                Up -> Pt(pt.x, pt.y - 1)
            }
        }

        fun isHorizontalMovement(): Boolean {
            return this is Left || this is Right
        }

        fun rotate(instr: String): Direction {
            return when (instr) {
                "R" -> rotateClockwise()
                "L" -> rotateAnticlockwise()
                else -> error("Unknown instruction $instr")
            }
        }

        private fun rotateClockwise(): Direction {
            return when (this) {
                Down -> Left
                Left -> Up
                Right -> Down
                Up -> Right
            }
        }

        private fun rotateAnticlockwise(): Direction {
            return when (this) {
                Down -> Right
                Left -> Down
                Right -> Up
                Up -> Left
            }
        }
    }

    fun part1(input: String) {
        val (map, commands) = input.split("\n\n")

        val regex = "[A-Z]{1}|\\d+".toRegex()
        val instructions = regex.findAll(commands).toList().map { it.value }

        val xValues = HashMap<Int, Set<Tile>>()
        val yValues = HashMap<Int, Set<Tile>>()
        var sp: Pt? = null

        map.split("\n").forEachIndexed { y, l ->
            for ((x, c) in l.withIndex()) {
                if (c == ' ') continue
                val tile = when (c) {
                    '.' -> {
                        if (sp == null) sp = Pt(x, y)
                        Tile.Empty(x, y)
                    }
                    '#' -> Tile.Wall(x, y)
                    else -> error("Unknown tile $c")
                }
                xValues.replace(x, xValues.getOrPut(x) { setOf() } + tile)
                yValues.replace(y, yValues.getOrPut(y) { setOf() } + tile)
            }
        }

        var currentDirection: Direction = Direction.Right

        fun getPositionOrNull(nc: Pt, tile: Tile?, roundedTile: Tile): Pt? {
            return if (tile != null) {
                if (tile.canMove()) nc
                else null
            } else {
                if (roundedTile.canMove()) Pt(roundedTile.x, roundedTile.y)
                else null
            }
        }
        instructions.forEach { ins ->
            val mov = ins.toIntOrNull()
            if (mov != null) {
                for (i in 0 until mov) {
                    val nc = currentDirection.move(sp!!)
                    if (currentDirection.isHorizontalMovement()) {
                        val tile: Tile? = yValues.getValue(nc.y).firstOrNull { it.x == nc.x }
                        val np = if (currentDirection is Direction.Left) getPositionOrNull(nc, tile, yValues.getValue(nc.y).last())
                        else getPositionOrNull(nc, tile, yValues.getValue(nc.y).first())
                        np?.let { sp = it } ?: break
                    } else {
                        val tile = xValues.getValue(nc.x).firstOrNull { it.y == nc.y }
                        val np =
                            if (currentDirection is Direction.Up) getPositionOrNull(nc, tile, xValues.getValue(nc.x).last())
                            else getPositionOrNull(nc, tile, xValues.getValue(nc.x).first())
                        np?.let { sp = it } ?: break
                    }
                }
            } else {
                currentDirection = currentDirection.rotate(ins)
            }
        }

        val ans = 1000 * (sp!!.y + 1) + 4 * (sp!!.x + 1) + currentDirection.facingValue()
        println(ans)
    }

    fun part2(input: String) {
        val (map, commands) = input.split("\n\n")

        val regex = "[A-Z]{1}|\\d+".toRegex()
        val instructions = regex.findAll(commands).toList().map { it.value }

        val xValues = HashMap<Int, Set<Tile>>()
        val yValues = HashMap<Int, Set<Tile>>()
        var sp: Pt? = null

        map.split("\n").forEachIndexed { y, l ->
            for ((x, c) in l.withIndex()) {
                if (c == ' ') continue
                val tile = when (c) {
                    '.' -> {
                        if (sp == null) sp = Pt(x, y)
                        Tile.Empty(x, y)
                    }
                    '#' -> Tile.Wall(x, y)
                    else -> error("Unknown tile $c")
                }
                xValues.replace(x, xValues.getOrPut(x) { setOf() } + tile)
                yValues.replace(y, yValues.getOrPut(y) { setOf() } + tile)
            }
        }

        var currentDirection: Direction = Direction.Right
        val cubeWidth = 50
        fun getPositionOrNull(sp: Pt, nc: Pt, tile: Tile?, direction: Direction): Pair<Pt, Direction>? {
            return if (tile != null) {
                if (tile.canMove()) nc to direction
                else null
            } else {
                fun getCubeSide(sp: Pt): Int {
                    return when {
                        sp.x in cubeWidth until cubeWidth * 2 && sp.y in 0 until cubeWidth -> 1
                        sp.x in cubeWidth * 2 until cubeWidth * 3 && sp.y in 0 until cubeWidth -> 2
                        sp.x in cubeWidth until cubeWidth * 2 && sp.y in cubeWidth until cubeWidth * 2 -> 3
                        sp.x in cubeWidth until cubeWidth * 2 && sp.y in cubeWidth * 2 until cubeWidth * 3 -> 4
                        sp.x in 0 until cubeWidth && sp.y in cubeWidth * 2 until cubeWidth * 3 -> 5
                        sp.x in 0 until cubeWidth && sp.y in cubeWidth * 3 until cubeWidth * 4 -> 6
                        else -> error("Unknown cube side $sp")
                    }
                }

                val cubeSide = getCubeSide(sp)
                val (p, d) = when (cubeSide to direction) {
                    1 to Direction.Up -> Pt(0, (2 * cubeWidth) + sp.x) to Direction.Right
                    1 to Direction.Left -> Pt(0, (3*cubeWidth)-sp.y-1) to Direction.Right // Offset
                    2 to Direction.Up -> Pt(sp.x - (2*cubeWidth), 4*cubeWidth-1) to Direction.Up
                    2 to Direction.Down -> Pt(2 * cubeWidth-1, sp.x - cubeWidth) to Direction.Left
                    2 to Direction.Right -> Pt(2 * cubeWidth-1, (3*cubeWidth)-sp.y-1) to Direction.Left // Offset
                    3 to Direction.Left -> Pt(sp.y - cubeWidth, 2 * cubeWidth) to Direction.Down
                    3 to Direction.Right -> Pt(sp.y + cubeWidth, cubeWidth-1) to Direction.Up
                    4 to Direction.Down -> Pt(cubeWidth-1, sp.x + (2*cubeWidth)) to Direction.Left
                    4 to Direction.Right -> Pt(3 * cubeWidth-1, cubeWidth-(sp.y-2*cubeWidth)-1) to Direction.Left // Offset
                    5 to Direction.Up -> Pt(cubeWidth, sp.x + cubeWidth) to Direction.Right
                    5 to Direction.Left -> Pt(cubeWidth, cubeWidth-(sp.y-2*cubeWidth)-1) to Direction.Right // Offset
                    6 to Direction.Down -> Pt(sp.x + (cubeWidth * 2), 0) to Direction.Down
                    6 to Direction.Left -> Pt(sp.y - (cubeWidth * 2), 0) to Direction.Down
                    6 to Direction.Right -> Pt(sp.y - (2 * cubeWidth), 3 * cubeWidth-1) to Direction.Up
                    else -> error("Wrong.")
                }
                val ft = xValues.getValue(p.x).first { it.y == p.y }
                if (ft.canMove()) p to d
                else null
            }
        }

        instructions.forEach { ins ->
            val mov = ins.toIntOrNull()
            if (mov != null) {
                for (i in 0 until mov) {
                    val nc = currentDirection.move(sp!!)
                    if (currentDirection.isHorizontalMovement()) {
                        val tile: Tile? = yValues.getValue(nc.y).firstOrNull { it.x == nc.x }
                        val np = getPositionOrNull(sp!!, nc, tile, currentDirection)
                        np?.let {(p, d) ->
                            sp = p
                            currentDirection = d
                        } ?: break
                    } else {
                        val tile = xValues.getValue(nc.x).firstOrNull { it.y == nc.y }
                        val np = getPositionOrNull(sp!!, nc, tile, currentDirection)
                        np?.let { (p, d) ->
                            sp = p
                            currentDirection = d
                        } ?: break
                    }
                }
            } else {
                currentDirection = currentDirection.rotate(ins)
            }
        }

        val ans = 1000 * (sp!!.y + 1) + 4 * (sp!!.x + 1) + currentDirection.facingValue()
        println(ans)
    }
}