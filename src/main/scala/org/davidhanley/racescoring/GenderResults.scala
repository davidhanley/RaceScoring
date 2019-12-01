package org.davidhanley.racescoring

import scala.collection.mutable.ArrayBuffer

case class GenderResults(gender: Char, pointsBase: Int, results: Seq[(Int, Double, Athlete)]) {

}

object GenderResults {

  def parseGenderResults(db: AthleteDB, gender: Char, pointsBase: Int, rows: Seq[Array[String]]): GenderResults = {

    val pbf = pointsBase * 5.0

    def scoreForRank(rank: Int): Double = {
      pbf / (rank + 4.0)
    }

    val ab = ArrayBuffer[(Int, Double, Athlete)]()

    var rank = 1
    for (row <- rows) {
      try {
        if (row(3)(0) == gender) {
          val v = db.lookUpAthlete(row(1), row(2).toInt)
          ab.append((rank, scoreForRank(rank), v))
          rank = rank + 1
        }
      } catch {
        case e: Throwable => println("Exception:" + e + " -- " + row)
      }
    }
    GenderResults(gender, pointsBase, ab.toSeq)

  }
}