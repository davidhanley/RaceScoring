package org.davidhanley.racescoring

import scala.collection.mutable

case class Athlete(id: Int, name: String, sex: Char, birthYear: Int) {
  override def hashCode(): Int = {
    name.hashCode * sex.hashCode()
  }
}

class AthleteDB(sex: Char) {

  val db = mutable.Map[String, Set[Athlete]]()
  var id = 1

  def lookUpAthlete(name: String, birthYear: Int): Athlete = {
    var aset = db.getOrElse(name, Set.empty)
    for (ath <- aset) {
      ath.birthYear match {

        case 0 => aset -= ath
          val a2 = new Athlete(ath.id, name, sex, birthYear)
          aset += a2
          db(name) = aset
          return a2
        case by => if (Math.abs(by - birthYear) <= 2 || birthYear == 0) {
          return ath
        }
      }

    }
    val ath = new Athlete(id, name, sex, birthYear)
    id = id + 1
    aset += ath
    db(name) = aset
    ath
  }
}
