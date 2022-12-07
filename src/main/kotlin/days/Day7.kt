package days

object Day7 {

    fun part1(input: List<String>) {
        val d = mutableListOf<String>()
        val output = HashMap<String, Long>()
        for (l in input) {
            if (l.startsWith("$ cd")) {
                val (_, _, dir) = l.split(" ")
                if (dir == "..") d.removeLast()
                else d.add(d.joinToString("") + dir)
            } else {
                val s = l.split(" ").mapNotNull { it.toLongOrNull() }.firstOrNull() ?: 0L
                d.forEach { cd ->
                    output.replace(cd, output.getOrPut(cd) { 0L } + s)
                }
            }
        }
        val ans = output.values.sumOf { if (it < 100000) it else 0 }
        println(ans)
    }

    fun part2(input: List<String>) {
        val d = mutableListOf<String>()
        val output = HashMap<String, Long>()
        for (l in input) {
            if (l.startsWith("$ cd")) {
                val (_, _, dir) = l.split(" ")
                if (dir == "..") d.removeLast()
                else d.add(d.joinToString("") + dir)
            } else {
                val s = l.split(" ").mapNotNull { it.toLongOrNull() }.firstOrNull() ?: 0L
                d.forEach { cd ->
                    output.replace(cd, output.getOrPut(cd) { 0L } + s)
                }
            }
        }
        val s = output.filterKeys { it == "/" }.values.first()
        val rs = 30000000 - (70000000 - s)
        val ans = output.values.filter { it >= rs }.minOf { it }
        println(ans)
    }

}