package days

// BFSa - Breadth first search
// https://www.kodeco.com/books/data-structures-algorithms-in-kotlin/v1.0/chapters/20-breadth-first-search
// https://www.geeksforgeeks.org/shortest-distance-two-cells-matrix-grid/
object Day12 {

    fun part1(input: List<String>) {
        data class Item(val x: Int, val y: Int, val dist: Int = 0)
        var sourceItem = Item(0, 0, 0)
        var (ex, ey) = 0 to 0
        val matrix = Array(input.size) { IntArray(input[0].length) }
        val visited = Array(input.size) { BooleanArray(input[0].length) }

        for((y, l) in input.withIndex()) {
            for((x, c) in l.asIterable().withIndex()) {
                if(c == 'S') {
                    sourceItem = Item(x, y, 0)
                    matrix[y][x] = 'a'-'a'
                    continue
                }
                if(c == 'E') {
                    ex = x
                    ey = y
                    matrix[y][x] = 'z'-'a'
                    continue
                }
                matrix[y][x] = c - 'a'
            }
        }
        visited[sourceItem.y][sourceItem.x] = true
        val queue = ArrayDeque<Item>()
        queue.addLast(sourceItem)

        fun isValid(cv: Int, x: Int, y:Int, matrix: Array<IntArray>, visited: Array<BooleanArray>): Boolean {
            return x >= 0 && y >= 0 && x < matrix[0].size && y < matrix.size && !visited[y][x] && matrix[y][x] - cv <= 1
        }

        while(!queue.isEmpty()) {
            val (cx, cy, distance) = queue.removeFirst()
            val currentValue = matrix[cy][cx]
            if(cx == ex && cy == ey) {
                println(distance)
                break
            }

            if(isValid(currentValue, cx, cy-1, matrix, visited)) {
                queue.addLast(Item(cx, cy-1, distance+1))
                visited[cy-1][cx] = true
            }

            if(isValid(currentValue, cx, cy+1, matrix, visited)) {
                queue.addLast(Item(cx, cy+1, distance+1))
                visited[cy+1][cx] = true
            }

            if(isValid(currentValue, cx-1, cy, matrix, visited)) {
                queue.addLast(Item(cx-1, cy, distance+1))
                visited[cy][cx-1] = true
            }

            if(isValid(currentValue, cx+1, cy, matrix, visited)) {
                queue.addLast(Item(cx+1, cy, distance+1))
                visited[cy][cx+1] = true
            }
        }
    }

    fun part2(input: List<String>) {
        data class Item(val x: Int, val y: Int, val dist: Int = 0)
        var (ex, ey) = 0 to 0
        val matrix = Array(input.size) { IntArray(input[0].length) }
        val startingPoints = mutableListOf<Item>()

        for((y, l) in input.withIndex()) {
            for((x, c) in l.asIterable().withIndex()) {
                if(c == 'S') {
                    startingPoints.add(Item(x, y, 0))
                    matrix[y][x] = 'a'-'a'
                    continue
                }
                if(c == 'E') {
                    ex = x
                    ey = y
                    matrix[y][x] = 'z'-'a'
                    continue
                }
                if(c == 'a') {
                    startingPoints.add(Item(x, y, 0))
                }
                matrix[y][x] = c - 'a'
            }
        }

        fun isValid(cv: Int, x: Int, y:Int, matrix: Array<IntArray>, visited: Array<BooleanArray>): Boolean {
            return x >= 0 && y >= 0 && x < matrix[0].size && y < matrix.size && !visited[y][x] && matrix[y][x] - cv <= 1
        }

        fun findDistance(source: Item): Int {
            val visited = Array(input.size) { BooleanArray(input[0].length) }
            visited[source.y][source.x] = true
            val queue = ArrayDeque<Item>()
            queue.addLast(source)
            while (!queue.isEmpty()) {
                val (cx, cy, distance) = queue.removeFirst()
                val currentValue = matrix[cy][cx]
                if (cx == ex && cy == ey) {
                    return distance
                }

                if (isValid(currentValue, cx, cy - 1, matrix, visited)) {
                    queue.addLast(Item(cx, cy - 1, distance + 1))
                    visited[cy - 1][cx] = true
                }

                if (isValid(currentValue, cx, cy + 1, matrix, visited)) {
                    queue.addLast(Item(cx, cy + 1, distance + 1))
                    visited[cy + 1][cx] = true
                }

                if (isValid(currentValue, cx - 1, cy, matrix, visited)) {
                    queue.addLast(Item(cx - 1, cy, distance + 1))
                    visited[cy][cx - 1] = true
                }

                if (isValid(currentValue, cx + 1, cy, matrix, visited)) {
                    queue.addLast(Item(cx + 1, cy, distance + 1))
                    visited[cy][cx + 1] = true
                }
            }
            return Int.MAX_VALUE
        }

        val ans = startingPoints.map { findDistance(it) }.minByOrNull { it }!!
        println(ans)
    }
}