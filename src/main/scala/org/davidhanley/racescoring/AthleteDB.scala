package org.davidhanley.racescoring

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class Athlete(id: Int, name: String, sex: Char, birthYear: Int) {

  var results: ArrayBuffer[(String, Int, Double)] = ArrayBuffer()

  def addResult(raceName: String, rank: Int, points: Double) = {
    results.append((raceName, rank, points))
  }

}

class AthleteDB {

  val db = mutable.Map[String, Set[Athlete]]()
  var id = 1

  def lookUpAthlete(name: String, sex:Char, birthYear: Int): Athlete = {
    val athleteKey = name + ":" + sex
    var aset = db.getOrElse(athleteKey, Set.empty)
    for (ath <- aset) {
      ath.birthYear match {

        case 0 => aset -= ath
          val a2 = new Athlete(ath.id, name, sex, birthYear)
          aset += a2
          db(athleteKey) = aset
          return a2
        case by => if (Math.abs(by - birthYear) <= 2 || birthYear == 0) {
          return ath
        }
      }

    }
    val ath = new Athlete(id, name, sex, birthYear)
    id = id + 1
    aset += ath
    db(athleteKey) = aset
    ath
  }
}
