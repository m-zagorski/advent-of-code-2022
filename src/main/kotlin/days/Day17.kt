package days

import readFile

object Day17 {

    operator fun invoke() {
        print("Part 1: ")
        part1(readFile("17"))
        print("Part 2: ")
        part2_weird(readFile("17"))
    }

    fun part1(input: String) {
        data class Pt(val x: Int, val y: Int)
        data class Figure(val pts: List<Pt>) {
            fun setStartPosition(sx: Int, sy: Int): Figure {
                return copy(pts = pts.map { it.copy(x = it.x + sx, y = it.y + sy) })
            }

            fun afterHorizontalMovement(mx: Int): List<Pt> {
                return if (mx < 0 && pts.minOf { it.x } > 0 || mx > 0 && pts.maxOf { it.x } < 6) {
                    pts.map { it.copy(x = it.x + mx) }
                } else {
                    pts
                }
            }

            fun afterMoveDown(): List<Pt> {
                return pts.map { it.copy(y = it.y - 1) }
            }
        }

        val figures = listOf(
            Figure(listOf(Pt(0, 0), Pt(1, 0), Pt(2, 0), Pt(3, 0))),
            Figure(listOf(Pt(1, 0), Pt(0, 1), Pt(1, 1), Pt(2, 1), Pt(1, 2))),
            Figure(listOf(Pt(0, 0), Pt(1, 0), Pt(2, 0), Pt(2, 1), Pt(2, 2))),
            Figure(listOf(Pt(0, 0), Pt(0, 1), Pt(0, 2), Pt(0, 3))),
            Figure(listOf(Pt(0, 0), Pt(1, 0), Pt(0, 1), Pt(1, 1)))
        )

        val movementPattern = input.asIterable().toList()

        fun figureCanGoDown(pts: List<Pt>, xValues: HashMap<Int, Set<Int>>): Boolean {
            pts.forEach { (x, y) ->
                if (y < 0) return false
                if (xValues[x]?.contains(y) == true) return false
            }
            return true
        }

        fun figureCanGoHorizontal(pts: List<Pt>, yValues: HashMap<Int, Set<Int>>): Boolean {
            pts.forEach { (x, y) ->
                if (yValues[y]?.contains(x) == true) return false
            }
            return true
        }


        var fl = 0
        val xValues = HashMap<Int, Set<Int>>()
        val yValues = HashMap<Int, Set<Int>>()

        fun addValue(pts: List<Pt>) {
            pts.forEach { (x, y) ->
                xValues.replace(x, xValues.getOrPut(x) { setOf() } + y)
                yValues.replace(y, yValues.getOrPut(y) { setOf() } + x)
            }
        }

        var figure = figures[fl % 2].setStartPosition(2, 3)
        var index = 0
        while (true) {
            val isPush = index % 2 == 0

            if (isPush) {
                val movement = when (movementPattern[(index / 2) % movementPattern.size]) {
                    '>' -> 1
                    '<' -> -1
                    else -> error("Unknown command")
                }
                val newPts = figure.afterHorizontalMovement(movement)
                if (figureCanGoHorizontal(newPts, yValues)) {
                    figure = figure.copy(pts = newPts)
                }
            } else {
                val newPts = figure.afterMoveDown()
                if (figureCanGoDown(newPts, xValues)) {
                    figure = figure.copy(pts = newPts)
                } else {
                    addValue(figure.pts)
                    fl++
                    val bottom = yValues.keys.maxOf { it }
                    figure = figures[fl % figures.size].setStartPosition(2, bottom + 1 + 3)
                    if (fl == 120) break
                }
            }
            index++
        }
        println(yValues.keys.maxOf { it } + 1)
    }

    sealed class CalculationResult {
        abstract val h: Int

        data class Loop(
            val p1: Long,
            val p2: Long,
            val firstLoopInstruction: Int,
            val p1Height: Int,
            val blockDiff: Long,
            val heightDiff: Int
        ) : CalculationResult() {
            override val h: Int
                get() = error("Should not be called")
        }

        data class Height(override val h: Int) : CalculationResult()
    }

    fun part2_weird(input: String) {
        data class Pt(val x: Int, val y: Int)
        data class Figure(val pts: List<Pt>) {
            fun setStartPosition(sx: Int, sy: Int): Figure {
                return copy(pts = pts.map { it.copy(x = it.x + sx, y = it.y + sy) })
            }

            fun afterHorizontalMovement(mx: Int): List<Pt> {
                return if (mx < 0 && pts.minOf { it.x } > 0 || mx > 0 && pts.maxOf { it.x } < 6) {
                    pts.map { it.copy(x = it.x + mx) }
                } else {
                    pts
                }
            }

            fun afterMoveDown(): List<Pt> {
                return pts.map { it.copy(y = it.y - 1) }
            }
        }

        val figures = listOf(
            Figure(listOf(Pt(0, 0), Pt(1, 0), Pt(2, 0), Pt(3, 0))),
            Figure(listOf(Pt(1, 0), Pt(0, 1), Pt(1, 1), Pt(2, 1), Pt(1, 2))),
            Figure(listOf(Pt(0, 0), Pt(1, 0), Pt(2, 0), Pt(2, 1), Pt(2, 2))),
            Figure(listOf(Pt(0, 0), Pt(0, 1), Pt(0, 2), Pt(0, 3))),
            Figure(listOf(Pt(0, 0), Pt(1, 0), Pt(0, 1), Pt(1, 1)))
        )

        val movementPattern = input.asIterable().toList()

        fun figureCanGoDown(pts: List<Pt>, xValues: HashMap<Int, Set<Int>>): Boolean {
            pts.forEach { (x, y) ->
                if (y < 0) return false
                if (xValues[x]?.contains(y) == true) return false
            }
            return true
        }

        fun figureCanGoHorizontal(pts: List<Pt>, yValues: HashMap<Int, Set<Int>>): Boolean {
            pts.forEach { (x, y) ->
                if (yValues[y]?.contains(x) == true) return false
            }
            return true
        }


        fun calculateResult(blocks: Long, instructionOffset: Int = 0, findCache: Boolean = false): CalculationResult {
            val xValues = HashMap<Int, Set<Int>>()
            val yValues = HashMap<Int, Set<Int>>()

            fun addValue(pts: List<Pt>) {
                pts.forEach { (x, y) ->
                    xValues.replace(x, xValues.getOrPut(x) { setOf() } + y)
                    yValues.replace(y, yValues.getOrPut(y) { setOf() } + x)
                }
            }

            data class FigureCache(val figureIndex: Int, val figureXValues: List<Int>, val instructionIndex: Int = 0)

            val figureCache = hashMapOf<FigureCache, Long>()
            var fl = 0L
            var figure = figures[(fl % 2).toInt()].setStartPosition(2, 3)
            var index = 0
            var firstLoopInstructions: Int? = null
            while (true) {
                val isPush = index % 2 == 0

                if (isPush) {
                    val movement = when (movementPattern[(index / 2 + instructionOffset) % movementPattern.size]) {
                        '>' -> 1
                        '<' -> -1
                        else -> error("Unknown command")
                    }
                    val newPts = figure.afterHorizontalMovement(movement)
                    if (figureCanGoHorizontal(newPts, yValues)) {
                        figure = figure.copy(pts = newPts)
                    }
                } else {
                    val newPts = figure.afterMoveDown()
                    if (figureCanGoDown(newPts, xValues)) {
                        figure = figure.copy(pts = newPts)
                    } else {
                        addValue(figure.pts)

                        fl++
                        if (fl % figures.size == 0L && findCache) {
                            val instIndex = (index / 2L).toInt() % movementPattern.size
                            val fc = FigureCache(
                                (fl % figures.size).toInt(),
                                figure.pts.map { it.x },
                                instructionIndex = instIndex
                            )
                            if (figureCache.contains(fc)) {
                                if (firstLoopInstructions == null) firstLoopInstructions = instIndex
                                val previous = figureCache.getValue(fc)
                                val diff = fl - previous
                                val previousHeight = calculateResult(previous).h
                                val currentHeight = calculateResult(fl).h
                                val heightDiff1 = currentHeight - previousHeight
                                val heightDiff2 = calculateResult(fl + diff).h - currentHeight

                                if (heightDiff1 == heightDiff2) {
                                    return CalculationResult.Loop(
                                        p1 = previous,
                                        p2 = fl,
                                        firstLoopInstruction = firstLoopInstructions,
                                        p1Height = previousHeight,
                                        blockDiff = diff,
                                        heightDiff = heightDiff1
                                    )
                                }
                            } else {
                                figureCache[fc] = fl
                            }
                        }
                        val bottom = yValues.keys.maxOf { it }
                        figure = figures[(fl % figures.size).toInt()].setStartPosition(2, bottom + 1 + 3)
                        if (fl == blocks && !findCache) return CalculationResult.Height(yValues.keys.maxOf { it } + 1)
                    }
                }
                index++
            }
        }

        val result = calculateResult(0, 0, true) as CalculationResult.Loop
        val fullCycles = (1000000000000 - result.p1) / result.blockDiff
        val roundsLeft: Long = 1000000000000 - (fullCycles * result.blockDiff + result.p1)
        val resultForLeftRounds = calculateResult(roundsLeft, result.firstLoopInstruction + 1).h
        val ans = fullCycles * result.heightDiff + result.p1Height + resultForLeftRounds
        println(ans)
    }
}