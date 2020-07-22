package org.davidhanley.racescoring


case class Race(name: String, points: Int, date: String)


object RaceParser {

  def parseRace(lines: Iterator[String]) = {

    val name = lines.next()
    val date = lines.next()
    val url = lines.next()
    val points = lines.next().toInt

    val rest: Seq[Array[String]] = lines.toSeq.map(line => line.toUpperCase().split(","))

    //val db = new AthleteDB()
    //val fr = GenderResults.parseGenderResults(name,)

    Race(name, points, date)


  }

}

object ATH {

  private def RotateRight[K,V](node: Node[K,V]) = { //       (5)            4
    //       / \           / \
    //      4   D         /   \
    //     / \           3     5
    //    3   C    -->  / \   / \
    //   / \           A   B C   D
    //  A   B
    val L = node.left.left
    val R = new Node[K,V](node.key, node.value, node.left.right, node.right)
    val N = new Node[K,V](node.left.key, node.left.value, L, R)
    N
  }

  def rotateLeft[K,V](node: Node[K,V]) = {
    //    (3)               4
    //    / \              / \
    //   A   4            /   \
    //      / \          3     5
    //     B   5   -->  / \   / \
    //        / \      A   B C   D
    //       C   D
    val L = new Node[K,V](node.key, node.value, node.left, node.right.left)
    val R = node.right.right
    val N = new Node[K,V](node.right.key, node.right.value, L, R)
    N
  }
}

case class Node[K, V](key: K, value: V, left: Node[K, V], right: Node[K, V]) {
  val height = {
    val lh = if (left == null) 0 else left.height
    val rh = if (left == null) 0 else right.height
    Math.max(lh, rh) + 1
  }
}


