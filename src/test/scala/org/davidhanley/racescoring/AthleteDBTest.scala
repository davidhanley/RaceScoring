package org.davidhanley.racescoring

import org.scalatest.{FlatSpec, Matchers}

class AthleteDBTest  extends FlatSpec with Matchers {


  "athlete DB" should "issue id's correctly" in {

    val db = new AthleteDB('f')

    {
      val a1 = db.lookUpAthlete("Erin Brand", 0)

      a1.id shouldBe 1
      a1.birthYear shouldBe 0
    }

    {
      val a1 = db.lookUpAthlete("Erin Brand", 1969)

      a1.id shouldBe 1
      a1.birthYear shouldBe 1969
    }

    {
      val a1 = db.lookUpAthlete("Erin Brand", 1970)

      a1.id shouldBe 1
      a1.birthYear shouldBe 1969
    }

    {
      val a1 = db.lookUpAthlete("David Hanley", 1972)

      a1.id shouldBe 2
      a1.birthYear shouldBe 1972
    }

    {
      val a1 = db.lookUpAthlete("David Hanley", 0)

      a1.id shouldBe 2
      a1.birthYear shouldBe 1972
    }

    {
      val a1 = db.lookUpAthlete("David Hanley", 1973)

      a1.id shouldBe 2
      a1.birthYear shouldBe 1972
    }

  }
}
