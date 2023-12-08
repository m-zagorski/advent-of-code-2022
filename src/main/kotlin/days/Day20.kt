@file:Suppress("DuplicatedCode")

package days

object Day20 {

    fun part1(input: List<String>) {
        data class V(val v: Int, val i: Int)

        val initial = input.mapIndexed { index, v ->
            V(v.toInt(), index)
        }

        val current: MutableList<V> = initial.toMutableList()
        val size = initial.size

        initial.forEach { c ->
            val index = current.indexOf(c)
            val newIndex = (index + c.v).mod(size-1)
            current.remove(c)
            current.add(newIndex, c)
        }

        val zeroIndex = current.indexOfFirst { it.v == 0 }
        val ans = listOf(1000, 2000, 3000).sumOf {
            val offset = size - ((size - zeroIndex) - it)
            val idx = offset % (size)
            current[idx].v
        }
        println(ans)
    }

    fun part2(input: List<String>) {
        data class V(val v: Long, val i: Int)

        val initial = input.mapIndexed { index, v ->
            V(v.toLong() * 811589153, index)
        }

        val current: MutableList<V> = initial.toMutableList()
        val size = initial.size

        repeat(10) {
            initial.forEach { c ->
                val index = current.indexOf(c)
                val newIndex = (index + c.v).mod(size - 1)
                current.remove(c)
                current.add(newIndex, c)
            }
        }

        val zeroIndex = current.indexOfFirst { it.v == 0L }
        val ans = listOf(1000, 2000, 3000).sumOf {
            val offset = size - ((size - zeroIndex) - it)
            val idx = offset % (size)
            current[idx].v
        }
        println(ans)
    }
}