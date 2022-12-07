package days

object Day2 {

    fun part1(input: List<String>) {
        val ans = input.sumOf { l ->
            val o = l[0] - 'A'
            val m = l[2] - 'X'
            val s = m + 1
            when {
                o == m -> s + 3
                m == (o + 1) % 3 -> s + 6
                else -> s
            }
        }
        println(ans)
    }

    fun part2(input: List<String>) {
        val ans = input.sumOf { l ->
            val o = l[0] - 'A'
            val m = when (l[2]) {
                'X' -> ((o + 2) % 3)
                'Y' -> o
                'Z' -> ((o + 1) % 3)
                else -> error("Wrong input")
            }
            val s = (m + 1)
            when {
                o == m -> (s + 3)
                m == (o + 1) % 3 -> (s + 6)
                else -> s
            }
        }
        println(ans)
    }
}