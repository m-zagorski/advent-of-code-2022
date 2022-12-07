package days

object Day5 {

    fun part1(input: String) {
        val (c, i) = input.split("\n\n")
        val g = HashMap<Int, ArrayDeque<String>>()
        c.split("\n")
            .reversed()
            .forEach { crate ->
                if (crate.startsWith(" 1")) return@forEach
                var empty = 0
                crate.split(" ").forEachIndexed { index, cc ->
                    if (cc.isNotBlank()) {
                        val newIndex = if (empty != 0) index - empty + (empty / 4) else index
                        g.getOrPut(newIndex + 1) { ArrayDeque() }.addLast(cc)
                    } else {
                        empty++
                    }

                }
            }

        i.split("\n")
            .forEach { l ->
                val (howMany, from, to) = l.split(" ").mapNotNull { it.toIntOrNull() }
                repeat((0 until howMany).count()) {
                    g[to]?.addLast(g[from]?.removeLastOrNull()!!)
                }
            }

        val ans = g.values.joinToString(separator = "") {
            it.last().replace("[", "").replace("]", "")
        }
        println(ans)
    }

    fun part2(input: String) {
        val (c, i) = input.split("\n\n")
        val result = List(9) { ArrayList<Char>() }
        c.split("\n")
            .forEach { l ->
                if (l.startsWith(" 1")) return@forEach
                for ((index, j) in (1 until l.length step 4).withIndex()) {
                    val cc = l[j]
                    if (!cc.isWhitespace()) {
                        result[index].add(0, cc)
                    }
                }
            }

        i.split("\n")
            .forEach { l ->
                val (howMany, from, to) = l.split(" ").mapNotNull { it.toIntOrNull() }
                val movingPart = result[from - 1].subList(result[from - 1].size - howMany, result[from - 1].size)
                result[to - 1] += movingPart
                movingPart.clear()
            }
        val ans = result.joinToString(separator = "") { it.last().toString() }
        println(ans)
    }
}