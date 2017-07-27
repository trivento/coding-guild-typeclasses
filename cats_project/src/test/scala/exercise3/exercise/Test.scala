package exercise3.exercise

import cats.implicits._
import cats.{Monoid, Semigroup}

import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {
  import Domain._

  it should "sum the total of an empty order" in {
    Order().total shouldBe 0
  }

  it should "sum the total of an order" in {
    Order(Map("cola" -> 1, "beer" -> 2, "wine" -> 3)).total shouldBe
      Catalog("cola") + Catalog("beer") * 2 + Catalog("wine") * 3
  }

  it should "be able to combine multiple tables to represent the bill for the combination of tables" in {
    val table1 = Table().order(Person("Michael") -> Order(Map("cola" -> 1, "wine" -> 1)))
                        .order(Person("John")    -> Order(Map("cola" -> 1, "beer" -> 2)))

    val table2 = Table().order(Person("Sophie")  -> Order(Map("wine" -> 1)))
                        .order(Person("Vanessa") -> Order(Map("beer" -> 1)))

    table1.combine(table2) shouldBe
      Table(
        Map(
          Person("Michael") -> Order(Map("cola" -> 1, "wine" -> 1)),
          Person("John")    -> Order(Map("cola" -> 1, "beer" -> 2)),
          Person("Sophie")  -> Order(Map("wine" -> 1)),
          Person("Vanessa") -> Order(Map("beer" -> 1))
        )
      )

    table1.combine(table2).total shouldBe 23.25
  }
}