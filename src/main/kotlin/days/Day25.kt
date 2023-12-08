package days

import kotlin.math.pow

object Day25 {
    fun part1(input: List<String>) {
        fun calcValue(t: String): Long {
            var sum = 0L
            for (i in (t.length - 1) downTo 0) {
                val value = when (val c = t[t.length - 1 - i]) {
                    '=' -> -2
                    '-' -> -1
                    else -> c.digitToInt()
                }
                val pow = 5.0.pow(i.toDouble()).toLong()
                sum += pow * value
            }
            return sum
        }

        var ans = input.sumOf { calcValue(it) }
        val digits = "=-012"
        val encoded = buildString {
            while (ans > 0) {
                val idx = (ans + 2) % 5
                ans = (ans + 2) / 5
                append(digits[idx.toInt()])
            }
        }.reversed()

        println(encoded)
    }
}