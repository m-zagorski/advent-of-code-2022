package days

object Day1 {

    fun part1(input: String): Int {
        return input.split("\n\n").maxOf {
            it.split("\n").sumOf { value -> value.toInt() }
        }
    }

    fun part2(input: String): Int {
        return input.split("\n\n")
            .map { it.split("\n").sumOf { value -> value.toInt() } }
            .sortedDescending()
            .take(3)
            .sum()
    }
}