package org.davidhanley.racescoring


case class Race(name: String, points: Int, date: String)

case class GenderResults(pointsBase: Int) {

}


object RaceParser {

  def parseRace(lines: Iterator[String]) = {

    val name = lines.next()
    val date = lines.next()
    val url = lines.next()
    val points = lines.next().toInt

    val rest = lines.toSeq

    Race(name, points, date)


  }

}
