package org.davidhanley.racescoring

import org.scalatest.{FlatSpec, Matchers}

//import scala.collection.JavaConverters._

/**
  * Created by dhanley on 6/22/17.
  */

class RaceLoadTest extends FlatSpec with Matchers {

  def openRace(): Iterator[String] = {
    scala.io.Source.fromFile("data/2019-esbru.csv").getLines()
  }

  "loading empire" should "get the header" in {
    val v = RaceParser.parseRace(openRace())

    v.name shouldBe "2019 Empire State Run Up"
    v.date shouldBe "2019-5-15"
    v.points shouldBe 200
  }


}

