package days

object Day10 {

    fun part1(input: List<String>) {
        var i = 1
        var v = 1
        var sc = 20
        var ans = 0
        fun check() {
            if (i == sc) {
                ans += i * v
                sc += 40
            }
        }
        for (l in input) {
            val ins = l.split(" ")
            var ic = 0
            var vc = 0
            when (ins[0]) {
                "noop" -> ic = 1
                "addx" -> {
                    ic = 2
                    vc = ins[1].toInt()
                }
            }

            repeat(ic) {
                check()
                i++
            }
            v += vc
        }
        println(ans)
    }

    fun part2(input: List<String>) {
        var i = 1
        var v = 1
        val a = Array(6) { CharArray(40) { '.' } }
        fun check() {
            val pos = (i - 1) % 40
            if (v in pos - 1..pos + 1) {
                a[(i - 1) / 40][pos] = '#'
            }
        }
        for (l in input) {
            val ins = l.split(" ")
            var ic = 0
            var vc = 0
            when (ins[0]) {
                "noop" -> ic = 1
                "addx" -> {
                    ic = 2
                    vc = ins[1].toInt()
                }
            }

            repeat(ic) {
                check()
                i++
            }
            v += vc
        }
        a.forEach { println(it.concatToString()) }
    }
}