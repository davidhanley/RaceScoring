package org.davidhanley.racescoring


import org.scalatest.{FlatSpec, Matchers}

class TestGenderResults extends FlatSpec with Matchers {

  "gender results" should "make sense" in {

    val db = new AthleteDB('F')

    val gr = GenderResults.parseGenderResults(db, 'F', 200,
      Seq(
        Array("", "Erin Brand", "1969", "F"),
        Array("", "Sue Glaser", "1975", "F"))
    )

    gr.results(0)._1 shouldBe 1
    gr.results(0)._2 shouldBe 200.0
    gr.results(0)._3.name shouldBe "Erin Brand"
    gr.results(0)._3.id shouldBe 1

    gr.results(1)._1 shouldBe 2
    gr.results(1)._2 shouldBe 166.66666666666666
    gr.results(1)._3.name shouldBe "Sue Glaser"
    gr.results(1)._3.id shouldBe 2
  }

}
