package days

object Day13 {

    private sealed class Value {
        abstract fun addValue(nv: Value)
        abstract fun compare(o: Value): ComparisonResult

        data class VInt(val v: Int) : Value() {
            override fun addValue(nv: Value) = error("Cant add value to int")

            override fun compare(o: Value): ComparisonResult {
                return when (o) {
                    is VList -> VList(mutableListOf(this)).compare(o)
                    is VInt -> {
                        val result = when {
                            v < o.v -> ComparisonResult.RIGHT_ORDER
                            v > o.v -> ComparisonResult.WRONG_ORDER
                            else -> ComparisonResult.CONTINUE
                        }
                        val result2 = v.compareTo(o.v)
                        result
                    }
                }
            }
        }

        data class VList(val v: MutableList<Value> = mutableListOf()) : Value() {
            override fun addValue(nv: Value) { v.add(nv) }

            override fun compare(o: Value): ComparisonResult {
                return when (o) {
                    is VInt -> compare(VList(mutableListOf(o)))
                    is VList -> compareElements(v, o.v)
                }
            }

            private fun compareElements(l: List<Value>, r: List<Value>): ComparisonResult {
                if (l.isEmpty() && r.isEmpty()) return ComparisonResult.CONTINUE
                if (l.isEmpty() && r.isNotEmpty()) return ComparisonResult.RIGHT_ORDER
                if (l.isNotEmpty() && r.isEmpty()) return ComparisonResult.WRONG_ORDER

                val lf = l.first()
                val rf = r.first()

                val result = lf.compare(rf)
                return if (result == ComparisonResult.CONTINUE) compareElements(l.minus(lf), r.minus(rf))
                else result
            }
        }
    }

    private enum class ComparisonResult {
        RIGHT_ORDER, WRONG_ORDER, CONTINUE
    }

    private sealed class Token {
        object Ls : Token()
        object Le : Token()
        data class Value(val v: Int) : Token()
    }

    private fun mapToTokens(s: String): List<Token> {
        val tryToParse = s.toIntOrNull()
        if (tryToParse != null) {
            return listOf(Token.Value(tryToParse))
        }
        var vv = ""
        val output = mutableListOf<Token>()
        for (ss in s) {
            when (ss) {
                '[' -> output.add(Token.Ls)
                ']' -> {
                    if (vv.isNotEmpty()) output.add(Token.Value(vv.toInt())).also { vv = "" }
                    output.add(Token.Le)
                }
                else -> vv += ss
            }
        }
        if (vv.isNotEmpty()) output.add(Token.Value(vv.toInt()))
        return output
    }

    private fun List<Token>.mapTokens(): MutableList<Value.VList> {
        val listStack = mutableListOf(Value.VList())
        for (token in this) {
            when (token) {
                is Token.Ls -> listStack.add(Value.VList())
                is Token.Le -> listStack.removeLast().also { listStack.last().addValue(it) }
                is Token.Value -> listStack.last().addValue(Value.VInt(token.v))
            }
        }
        return listStack
    }

    private fun mapToValues(s: String): Value {
        return s
            .drop(1)
            .dropLast(1)
            .split(",")
            .map(::mapToTokens)
            .flatten()
            .mapTokens()
            .first()
    }

    fun part1(input: String) {
        var ans = 0
        val pairs = input.split("\n\n")

        for ((pairIndex, pair) in pairs.withIndex()) {
            val (l, r) = pair.split("\n")
            val result = mapToValues(l).compare(mapToValues(r))
            if (result == ComparisonResult.RIGHT_ORDER) {
                ans += (pairIndex + 1)
            }
        }
        println(ans)
    }

    fun part2(input: String) {
        val pairs = input.split("\n\n")
        val fwp = mapToValues("[[2]]")
        val swp = mapToValues("[[6]]")

        fun Array<Value>.bubbleSort(): Array<Value> {
            for (i in 0 until size - 1) {
                for (j in 0 until size - i - 1) {
                    val r = this[j].compare(this[j + 1])
                    if (r == ComparisonResult.WRONG_ORDER) {
                        val tmp = this[j]
                        this[j] = this[j + 1]
                        this[j + 1] = tmp
                    }
                }
            }
            return this
        }

        val output = pairs
            .map { pair -> pair.split("\n").map(::mapToValues) }
            .flatten()
            .plus(fwp)
            .plus(swp)
            .toTypedArray()
            .bubbleSort()

        val a = output.indexOf(fwp) + 1
        val b = output.indexOf(swp) + 1
        val ans = a * b
        println(ans)
    }
}