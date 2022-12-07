package days

object Day6 {

    fun part1(input: String) {
        input.asIterable().windowed(4)
            .forEachIndexed { index, list ->
                if (list.toSet().size == 4) {
                    println(index + 4)
                    return
                }
            }
    }

    fun part2(input: String) {
        input.asIterable().windowed(14)
            .forEachIndexed { index, list ->
                if (list.toSet().size == 14) {
                    println(index + 14)
                    return
                }
            }
    }

    fun solution(input:String, n: Int) {
        val ans = input.windowed(n).indexOfFirst { it.toSet().size == it.length } + n
        println(ans)
    }
}