package days

object Day4 {

    fun part1(input: List<String>) {
        val ans = input.count { l ->
            val (left, right) = l.split(",")
            val (ls, le) = left.split("-").map { it.toInt() }
            val (rs, re) = right.split("-").map { it.toInt() }
            val f = ls <= rs && le >= re
            val s = rs <= ls && re >= le
            f || s
        }
        println(ans)
    }

    fun part2(input: List<String>) {
        val ans = input.count { l ->
            val (left, right) = l.split(",")
            val (ls, le) = left.split("-").map { it.toInt() }
            val (rs, re) = right.split("-").map { it.toInt() }
            rs in ls..le || re in ls..le || ls in rs..re || re in le..ls
        }
        println(ans)
    }
}