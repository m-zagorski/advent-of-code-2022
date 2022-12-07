package days

object Day3 {

    fun part1(input: List<String>) {
        val ans = input.sumOf { i ->
            val p = i.asIterable().toList()
            val (l, r) = p.chunked(p.size/2)
            val common = l.intersect(r)
            common.sumOf {
                when(it) {
                    in 'a'..'z' -> it - 'a' + 1
                    in 'A'..'Z' -> it - 'A' + 27
                    else -> error("Wrong character")
                }
            }
        }
        println(ans)
    }

    fun part2(input: List<String>) {
        val ans = input.chunked(3).sumOf { i ->
            val (f, s, t) = i.map { it.asIterable().toList() }
            val common = f.intersect(s).intersect(t)
            common.sumOf {
                when(it) {
                    in 'a'..'z' -> it - 'a' + 1
                    in 'A'..'Z' -> it - 'A' + 27
                    else -> error("Wrong character")
                }
            }
        }
        println(ans)
    }
}