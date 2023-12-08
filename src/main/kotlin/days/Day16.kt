package days

object Day16 {
    data class Node(val name: String, val adjacentNodes: List<Node> = emptyList())

    fun dijkstra(graph: Set<Node>, source: Node): MutableMap<String, Int> {
        val dist = mutableMapOf<String, Int>()
        val settledNodes = mutableSetOf<Node>()
        val unsettledNodes = mutableSetOf<Node>()

        graph.forEach { dist[it.name] = Int.MAX_VALUE }
        dist[source.name] = 0
        unsettledNodes.add(source)

        while (unsettledNodes.size != 0) {
            val currentNode = getLowestDistanceNode(dist, unsettledNodes)
            unsettledNodes.remove(currentNode)
            for (adjacentNode in graph.first { it.name == currentNode.name }.adjacentNodes) {
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(dist, adjacentNode, currentNode)
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
        }
        return dist
    }

    fun calculateMinimumDistance(dist: MutableMap<String, Int>, evaluationNode: Node, sourceNode: Node) {
        val sourceDistance = dist.getValue(sourceNode.name)
        if (sourceDistance + 1 < dist.getValue(evaluationNode.name)) {
            dist[evaluationNode.name] = sourceDistance + 1
        }
    }

    fun getLowestDistanceNode(dist: MutableMap<String, Int>, unsettledNodes: Set<Node>): Node {
        var lowestDistanceNode: Node? = null
        var lowestDistance = Integer.MAX_VALUE
        for (node in unsettledNodes) {
            if (dist.getValue(node.name) < lowestDistance) {
                lowestDistance = dist.getValue(node.name)
                lowestDistanceNode = node
            }
        }
        return lowestDistanceNode!!
    }

    fun part1(input: List<String>) {
        data class NodeDetails(val weight: Int, val connections: List<String>)

        val regex = "[A-Z]{2}|\\d+".toRegex()
        val nodes = mutableMapOf<String, NodeDetails>()
        val nodeValues = mutableMapOf<String, Int>()
        for (line in input) {
            val (l, r) = line.split(";")
            val (ln, lv) = regex.findAll(l).toList().map { it.value }
            val n = regex.findAll(r).toList().map { it.value }
            nodeValues[ln] = lv.toInt()
            nodes[ln] = NodeDetails(lv.toInt(), n)
        }

        val graphNodes = mutableSetOf<Node>()
        nodes.forEach { (node, details) ->
            graphNodes.add(Node(node, details.connections.map { Node(it) }))
        }

        fun pickNextNode(current: String): Triple<String, Int, Int> {
            val stepsRequired = dijkstra(graphNodes, graphNodes.first { it.name == current })
            val sortedValues = nodeValues.toList().sortedByDescending { (_, value) -> value }.toMap()
            // Name - Steps -- Weight
            var bestPick: Triple<String, Int, Int>? = null
            sortedValues.forEach check@{ k, v ->
                val stepsForMovement = stepsRequired.getValue(k)
                if (bestPick == null) {
                    bestPick = Triple(k, stepsForMovement, v)
                    return@check
                }

                if (bestPick!!.second > stepsForMovement) {
                    val newCandidateValue = (bestPick!!.second - stepsForMovement + 1) * v
                    if (newCandidateValue > bestPick!!.third) {
                        bestPick = Triple(k, stepsForMovement, v)
                    }
                }
            }
            return bestPick!!
        }

        var minutes = 30
        var currentNode = "AA"
        var pressureToBeReleased = 0
        var currentPressureReleased = 0
        while (minutes > 0) {
            if (nodeValues.any { (_, w) -> w > 0 }) {
                val (name, steps, weight) = pickNextNode(currentNode)
                currentPressureReleased += pressureToBeReleased * (steps + 1)
                minutes -= steps + 1
                currentNode = name
                pressureToBeReleased += weight
                nodeValues[currentNode] = 0
            } else {
                currentPressureReleased += pressureToBeReleased
                minutes--
            }
        }
        println("Total pressure released $currentPressureReleased")

    }

    fun part2(input: List<String>) {
        data class NodeDetails(val weight: Int, val connections: List<String>)

        val regex = "[A-Z]{2}|\\d+".toRegex()
        val nodes = mutableMapOf<String, NodeDetails>()
        val nodeValues = mutableMapOf<String, Int>()
        for (line in input) {
            val (l, r) = line.split(";")
            val (ln, lv) = regex.findAll(l).toList().map { it.value }
            val n = regex.findAll(r).toList().map { it.value }
            nodeValues[ln] = lv.toInt()
            nodes[ln] = NodeDetails(lv.toInt(), n)
        }

        val graphNodes = mutableSetOf<Node>()
        nodes.forEach { (node, details) ->
            graphNodes.add(Node(node, details.connections.map { Node(it) }))
        }

        data class BestNode(val name: String, val steps: Int, val weight: Int, val pressure: Int)
        data class BestPicks(
            val p1: BestNode,
            val p2: BestNode? = null
        )

        fun pickNextNode(current: String, minutesLeft: Int): BestPicks {
            val stepsRequired = dijkstra(graphNodes, graphNodes.first { it.name == current })
            val sortedValues = nodeValues.toList().sortedByDescending { (_, value) -> value }.toMap()
            // Name - Steps -- Weight
            val allPossibilities = mutableListOf<BestNode>()

            var bestPick: BestNode? = null
            sortedValues.forEach check@{ k, v ->
                val stepsForMovement = stepsRequired.getValue(k)
                if (bestPick == null) {
                    bestPick = BestNode(k, stepsForMovement, v, v)
                    allPossibilities.add(bestPick!!)
                    return@check
                }

                if (bestPick!!.steps > stepsForMovement) {
                    val cn = BestNode(k, stepsForMovement, v, (bestPick!!.steps - stepsForMovement + 1) * v)
                    if(stepsForMovement < minutesLeft) {
                        allPossibilities.add(cn)
                    }
                }
            }
            allPossibilities.sortByDescending { it.pressure }
            val possib = allPossibilities.take(2)
            return BestPicks(possib.first(), possib.minus(possib.first()).firstOrNull())
        }

        var playerMinutes = 26
        var elephantMinutes = 26

        var playerCurrentNode = "AA"
        var elephantCurrentNode = "AA"

        var playerPressureToBeReleased = 0
        var elephantPressureToBeReleased = 0

        var currentPressureReleased = 0
        while (true) {
            if (playerMinutes == 0 && elephantMinutes == 0) break

            if (nodeValues.any { (_, w) -> w > 0 }) {
                val (e1, e2) = pickNextNode(elephantCurrentNode, elephantMinutes)
                val (p1, p2) = pickNextNode(playerCurrentNode, playerMinutes)

                var elephantNode: BestNode
                var playerNode: BestNode
                if (e1.name == p1.name) {
                    if (e1.pressure >= p1.pressure) {
                        elephantNode = e1
                        playerNode = p2!!
                    } else {
                        elephantNode = e2!!
                        playerNode = p1
                    }
                } else {
                    elephantNode = e1
                    playerNode = p1
                }

                elephantCurrentNode = elephantNode.name
                playerCurrentNode = playerNode.name

                if(elephantMinutes > elephantNode.steps+1) {
                currentPressureReleased += elephantPressureToBeReleased * (elephantNode.steps + 1)
                elephantMinutes -= elephantNode.steps + 1
                elephantPressureToBeReleased += elephantNode.weight
                nodeValues[elephantNode.name] = 0
                } else if(elephantMinutes > 0) {
                    currentPressureReleased += elephantPressureToBeReleased
                    elephantMinutes--
                }

                if(playerMinutes > playerNode.steps+1) {
                currentPressureReleased += playerPressureToBeReleased * (playerNode.steps + 1)
                playerMinutes -= playerNode.steps + 1
                playerPressureToBeReleased += playerNode.weight
                nodeValues[playerNode.name] = 0
                } else if(playerMinutes > 0) {
                    currentPressureReleased += playerPressureToBeReleased
                    playerMinutes--
                }
            } else {
                if (playerMinutes > 0) {
                    currentPressureReleased += playerPressureToBeReleased
                    playerMinutes--
                }
                if (elephantMinutes > 0) {
                    currentPressureReleased += elephantPressureToBeReleased
                    elephantMinutes--
                }
            }

        }
        println("Total pressure released $currentPressureReleased")

    }
}