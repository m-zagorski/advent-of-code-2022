package days

object Day8 {

    fun part1(input: List<String>) {
        val yValues = HashMap<Int, List<Int>>()
        val xValues = HashMap<Int, List<Int>>()
        for ((y, l) in input.withIndex()) {
            val items: List<Int> = l.asIterable().map { it.digitToInt() }
            yValues[y] = items
            items.forEachIndexed { x, xv ->
                xValues.replace(x, xValues.getOrPut(x) { ArrayList() } + xv)
            }
        }

        var ans = xValues.size * 2 + (yValues.size - 2) * 2
        for (y in 1 until yValues.size - 1) {
            for (x in 1 until xValues.size - 1) {
                val v = yValues.getValue(y)[x]
                val t = xValues.getValue(x).subList(0, y).none { it >= v }
                val b = xValues.getValue(x).subList(y + 1, yValues.size).none { it >= v }
                val l = yValues.getValue(y).subList(0, x).none { it >= v }
                val r = yValues.getValue(y).subList(x + 1, xValues.size).none { it >= v }

                if (t || b || l || r) {
                    ans++
                    continue
                }
            }
        }
        println(ans)
    }

    fun part2(input: List<String>) {
        val yValues = HashMap<Int, List<Int>>()
        val xValues = HashMap<Int, List<Int>>()
        for ((y, l) in input.withIndex()) {
            val items: List<Int> = l.asIterable().map { it.digitToInt() }
            yValues[y] = items
            items.forEachIndexed { x, xv ->
                xValues.replace(x, xValues.getOrPut(x) { ArrayList() } + xv)
            }
        }

        var score = 0
        for (y in 1 until yValues.size - 1) {
            for (x in 1 until xValues.size - 1) {
                val v = yValues.getValue(y)[x]
                val t = xValues.getValue(x).subList(0, y).reversed()
                val b = xValues.getValue(x).subList(y + 1, yValues.size)
                val l = yValues.getValue(y).subList(0, x).reversed()
                val r = yValues.getValue(y).subList(x + 1, xValues.size)

                fun getScore(cv: Int, maxSize: Int): Int = if (cv == -1) maxSize else cv + 1
                val tmpScore = getScore(t.indexOfFirst { it >= v }, t.size) *
                        getScore(b.indexOfFirst { it >= v }, b.size) *
                        getScore(l.indexOfFirst { it >= v }, l.size) *
                        getScore(r.indexOfFirst { it >= v }, r.size)
                if (tmpScore > score) score = tmpScore
            }
        }
        println(score)
    }
}