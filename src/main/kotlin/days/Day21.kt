package days

import readLines

object Day21 {

    operator fun invoke() {
        print("Part 1: ")
        part1(readLines("21"))
        print("Part 2: ")
        part2(readLines("21"))
    }

    fun part1(input: List<String>) {
        data class Op(val l: String, val r: String, val op: String) {
            fun calc(left: Long, right: Long): Long {
                return when (op) {
                    "+" -> left + right
                    "-" -> left - right
                    "*" -> left * right
                    "/" -> left / right
                    else -> error("Unknown op $op")
                }
            }
        }

        val m = mutableMapOf<String, Any>()
        for (line in input) {
            val monkey = line.substringBefore(": ")
            val n = line.substringAfter(": ").trim().toLongOrNull()
            if (n != null) {
                m[monkey] = n
            } else {
                val (l, op, r) = line.substringAfter(": ").split(" ")
                m[monkey] = Op(l, r, op)
            }
        }

        fun findValue(monkey: String): Long {
            return when (val mv = m.getValue(monkey)) {
                is Long -> mv
                is Op -> mv.calc(findValue(mv.l), findValue(mv.r))
                else -> error("Unknown value")
            }
        }

        val ans = findValue("root")
        println(ans)
    }

    sealed class Node {
        abstract val lnode: Node?
        abstract val rnode: Node?

        data class Op(val op: String, override val lnode: Node? = null, override val rnode: Node? = null) : Node()
        data class Value(val v: Long, override val lnode: Node? = null, override val rnode: Node? = null) : Node()
    }

    fun part2(input: List<String>) {
        data class Op(val l: String, val r: String, val op: String)

        fun calc(left: Long, right: Long, op: String, debug: Boolean = false): Long {
            val a = when (op) {
                "+" -> left + right
                "-" -> left - right
                "*" -> left * right
                "/" -> left / right
                else -> error("Unknown op $op")
            }
            return a
        }

        fun calcOpposite(left: Long, right: Long, op: String, isLeft: Boolean): Long {
            return when (op) {
                "+" -> left - right
                "-" -> if(isLeft) right - left else left + right
                "*" -> left / right
                "/" -> left * right
                else -> error("Unknown op $op")
            }
        }

        val m = mutableMapOf<String, Any>()
        for (line in input) {
            val monkey = line.substringBefore(": ")
            val n = line.substringAfter(": ").trim().toLongOrNull()
            if (n != null) {
                m[monkey] = n
            } else {
                val (l, op, r) = line.substringAfter(": ").split(" ")
                m[monkey] = Op(l, r, op)
            }
        }

        fun findNodeValue(monkey: String): Node {
            if (monkey == "humn") return Node.Op("x")
            return when (val mv = m.getValue(monkey)) {
                is Long -> Node.Value(mv)
                is Op -> Node.Op(mv.op, findNodeValue(mv.l), findNodeValue(mv.r))
                else -> error("Unknown value")
            }
        }

        fun findPath(root: Node?, arr: ArrayList<String>, value: String): Boolean {
            if (root == null) return false

            if (root is Node.Op) {
                arr.add(value)
                if (root.op == "x") return true

                if (findPath(root.lnode, arr, "L") || findPath(root.rnode, arr, "R")) {
                    return true
                }
                arr.removeLast()
            }
            return false
        }

        fun calcNodeValue(node: Node?, debug: Boolean = false): Long {
            if (node == null) {
                if(debug) println("Node is null return 0")
                return 0
            }
            return when (node) {
                is Node.Value -> node.v.also { if(debug) println("Node is value ${node}") }
                is Node.Op -> calc(calcNodeValue(node.lnode, debug), calcNodeValue(node.rnode, debug), node.op, debug).also { if(debug) println("Node is op $node") }
            }
        }

        val v = m.getValue("root") as Op
        val l = findNodeValue(v.l)
        var r = calcNodeValue(findNodeValue(v.r))
        val path = ArrayList<String>()
        findPath(l, path, "S")

        var currentNode: Node.Op = l as Node.Op
        path.reverse()
        path.removeLast()
        while (true) {
            val left = currentNode.lnode
            val right = currentNode.rnode
            val op = currentNode.op

            if (op == "x") break

            val currentWay = path.removeLast()
            if (currentWay == "L") {
                val right1 = calcNodeValue(right)
                r = calcOpposite(r, right1, op, false)
                currentNode = left as Node.Op
            }
            if (currentWay == "R") {
                val right1 = calcNodeValue(left)
                r = calcOpposite(r, right1, op, true)
                currentNode = right as Node.Op
            }
        }
        println(r)
    }

}