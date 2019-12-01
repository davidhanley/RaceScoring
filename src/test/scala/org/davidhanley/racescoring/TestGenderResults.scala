package org.davidhanley.racescoring


import org.scalatest.{FlatSpec, Matchers}

class TestGenderResults extends FlatSpec with Matchers {

  "gender results" should "make sense" in {

    val db = new AthleteDB()

    val gr = GenderResults.parseGenderResults("FFA hanley", db, 'F', 200,
      Seq(
        Array("", "Erin Brand", "1969", "F"),
        Array("", "Sue Glaser", "1975", "F"))
    )

    {
      val a1 = gr.results(0)
      a1._1 shouldBe 1
      a1._2 shouldBe 200.0
      a1._3.name shouldBe "Erin Brand"
      a1._3.id shouldBe 1
      a1._3.results.size shouldBe 1

      gr.results(1)._1 shouldBe 2
      gr.results(1)._2 shouldBe 166.66666666666666
      gr.results(1)._3.name shouldBe "Sue Glaser"
      gr.results(1)._3.id shouldBe 2
    }


    //enter another race.  Erin should have two entries
    val gr2 = GenderResults.parseGenderResults("FFA dave", db, 'F', 100,
      Seq(
        Array("", "Erin Brand", "1969", "F"),
        Array("", "Karen Geninatti", "1965", "F"))
    )

    {
      val a1 = gr2.results(0)
      a1._1 shouldBe 1
      a1._2 shouldBe 100.0
      a1._3.name shouldBe "Erin Brand"
      a1._3.id shouldBe 1
      a1._3.results.size shouldBe 2

      gr2.results(1)._1 shouldBe 2
      gr2.results(1)._2 shouldBe 83.33333333333333
      gr2.results(1)._3.name shouldBe "Karen Geninatti"
      gr2.results(1)._3.id shouldBe 3
    }
  }



}
