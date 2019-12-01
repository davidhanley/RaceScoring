package org.davidhanley.racescoring

import scala.collection.mutable.ArrayBuffer

case class GenderResults(gender: Char, pointsBase: Int, results: Seq[(Int, Double, Athlete)]) {

}

object GenderResults {

  def parseGenderResults(race: String, db: AthleteDB, gender: Char, pointsBase: Int, rows: Seq[Array[String]]): GenderResults = {

    val pbf = pointsBase * 5.0

    def scoreForRank(rank: Int): Double = {
      pbf / (rank + 4.0)
    }

    val ab = ArrayBuffer[(Int, Double, Athlete)]()

    var rank = 1
    for (row <- rows) {
      try {
        if (row(3)(0) == gender) {
          val athlete = db.lookUpAthlete(row(1), row(3)(0), row(2).toInt)
          val points = scoreForRank(rank)
          ab.append((rank, points, athlete))
          athlete.addResult(row(1), rank, points)
          rank = rank + 1
        }
      } catch {
        case e: Throwable => println("Exception:" + e + " -- " + row)
      }
    }
    GenderResults(gender, pointsBase, ab.toSeq)

  }
}