package days

object Day13_b {

    private sealed class Value {
        fun compare(o: Value): Int {
            if(this is VInt && o is VInt) return v.compareTo(o.v)
            if(this is VList && o is VList) {
                var i = 0
                while(i < v.size && i < o.v.size) {
                    val result = v[i].compare(o.v[i])
                    if(result != 0) return result
                    i++
                }
                return v.size.compareTo(o.v.size)
            }
            if(this is VInt) return VList(mutableListOf(this)).compare(o)
            if(o is VInt) return compare(VList(mutableListOf(o)))
            error("Should not happen")
        }

        data class VInt(val v: Int) : Value()
        data class VList(val v: MutableList<Value> = mutableListOf()) : Value()
    }

    private fun mapToValue(s: String): Value {
        val regex = "\\d+|\\[|]".toRegex()
        val ro = regex.findAll(s)
        val listStack = mutableListOf(Value.VList())
        ro.toList().map { r ->
            when {
                r.value == "[" -> listStack.add(Value.VList())
                r.value == "]" -> listStack.removeLast().also(listStack.last().v::add)
                r.value.toIntOrNull() != null -> listStack.last().v.add(Value.VInt(r.value.toInt()))
                else -> error("!!!")
            }
        }
        return listStack.first()
    }

    fun part1(input: String) {
        var ans = 0
        val pairs = input.split("\n\n")

        for ((pairIndex, pair) in pairs.withIndex()) {
            val (l, r) = pair.split("\n")
            val result = mapToValue(l).compare(mapToValue(r))
            if (result == -1) {
                ans += (pairIndex + 1)
            }
        }
        println(ans)
    }

    fun part2(input: String) {
        val pairs = input.split("\n\n")
        val fwp = mapToValue("[[2]]")
        val swp = mapToValue("[[6]]")

        fun Array<Value>.bubbleSort(): Array<Value> {
            for (i in 0 until size - 1) {
                for (j in 0 until size - i - 1) {
                    val r = this[j].compare(this[j + 1])
                    if (r == 1) {
                        val tmp = this[j]
                        this[j] = this[j + 1]
                        this[j + 1] = tmp
                    }
                }
            }
            return this
        }

        val output = pairs
            .map { pair -> pair.split("\n").map(::mapToValue) }
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