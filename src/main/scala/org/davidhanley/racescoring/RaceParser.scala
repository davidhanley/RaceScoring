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
