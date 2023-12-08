package days

object Day11 {
    fun part1(input: String) {
        data class Monkey(
            val items: List<Int> = emptyList(),
            val div: Int = -1,
            val op: String = "",
            val v: String = "",
            val trueVal: Int = -1,
            val falseVal: Int = -1,
            var totalInspectionCount: Int = 0
        ) {
            fun getMonkey(item: Int): Pair<Int, Int> {
                totalInspectionCount++
                val nwl = updateWorryLevel(item)
                val boring = nwl / 3
                return monkeyToThrowTo(boring) to boring
            }

            private fun updateWorryLevel(wl: Int): Int {
                return when (op) {
                    "*" -> wl * getValue(wl)
                    "+" -> wl + getValue(wl)
                    else -> error("Unknown operation [$op]")
                }
            }

            private fun monkeyToThrowTo(wl: Int): Int {
                return if (wl % div == 0) trueVal
                else falseVal
            }

            private fun getValue(wl: Int) = if (v == "old") wl else v.toInt()
        }

        val monkeys = Array(8) { Monkey() }
        input.split("\n\n")
            .forEachIndexed { index, s ->
                val (items, operation, div, forTrue, forFalse) = s.split("\n").drop(1)
                val (_, _, _, op, n) = operation.substringAfterLast(": ").split(" ")
                val monkey = Monkey(
                    items = items.substringAfterLast(": ").split(",").map { it.trim().toInt() },
                    div = div.split(" ").last().toInt(),
                    op = op,
                    v = n,
                    trueVal = forTrue.split(" ").last().toInt(),
                    falseVal = forFalse.split(" ").last().toInt()
                )
                monkeys[index] = monkey
            }

        repeat(20) {
            for ((index, m) in monkeys.withIndex()) {
                for (item in m.items) {
                    val (mIndex, newItem) = m.getMonkey(item)
                    monkeys[mIndex] = monkeys[mIndex].copy(items = monkeys[mIndex].items.plus(newItem))
                }
                monkeys[index] = monkeys[index].copy(items = emptyList())
            }
        }

        val (f, s) = monkeys.sortedByDescending { it.totalInspectionCount }.take(2).map { it.totalInspectionCount }
        val ans = f * s
        println(ans)
    }

    fun part2(input: String) {
        var divNumber = 0L

        data class Monkey(
            val items: List<Long> = emptyList(),
            val div: Int = -1,
            val op: String = "",
            val v: String = "",
            val trueVal: Int = -1,
            val falseVal: Int = -1,
            var totalInspectionCount: Long = 0
        ) {
            fun getMonkey(item: Long): Pair<Int, Long> {
                totalInspectionCount++
                val nwl = updateWorryLevel(item)
                val calm = nwl % divNumber
                val monkeyToThrowTo = monkeyToThrowTo(calm)
                return monkeyToThrowTo to calm
            }


            private fun updateWorryLevel(wl: Long): Long {
                return when (op) {
                    "*" -> wl * getValue(wl)
                    "+" -> wl + getValue(wl)
                    else -> error("Unknown operation [$op]")
                }
            }

            private fun monkeyToThrowTo(wl: Long): Int {
                return if (wl % div == 0L) trueVal
                else falseVal
            }

            private fun getValue(wl: Long) = if (v == "old") wl else v.toLong()
        }

        val monkeys = Array(8) { Monkey() }
        input.split("\n\n")
            .forEachIndexed { index, s ->
                val (items, operation, div, forTrue, forFalse) = s.split("\n").drop(1)
                val (_, _, _, op, n) = operation.substringAfterLast(": ").split(" ")
                val monkey = Monkey(
                    items = items.substringAfterLast(": ").split(",").map { it.trim().toLong() },
                    div = div.split(" ").last().toInt(),
                    op = op,
                    v = n,
                    trueVal = forTrue.split(" ").last().toInt(),
                    falseVal = forFalse.split(" ").last().toInt()
                )
                monkeys[index] = monkey
            }

        fun findCommonDivisor(numbers: List<Int>): Long {
            var divisor = numbers.minOf{it}.toLong()
            while (true) {
                if (numbers.all { it % divisor == 0L }) {
                    return divisor
                }
                divisor++
            }
        }

        divNumber = monkeys.fold(1L) { acc, monkey -> acc * monkey.div }
        val dv2 = monkeys.map { it.div }.reduce(Int::times)
        repeat(10_000) {
            for ((index, m) in monkeys.withIndex()) {
                for (item in m.items) {
                    val (mIndex, newItem) = m.getMonkey(item)
                    monkeys[mIndex] = monkeys[mIndex].copy(items = monkeys[mIndex].items.plus(newItem))
                }
                monkeys[index] = monkeys[index].copy(items = emptyList())
            }
        }

        val (f, s) = monkeys.sortedByDescending { it.totalInspectionCount }.take(2).map { it.totalInspectionCount }
        val ans = f * s
        println(ans)
    }
}