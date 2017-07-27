package exercise1.exercise

import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {
  import Domain._

  it should "Add orders correctly" in {
    val order1 = Order(Map("cola" -> 1, "wine" -> 2))
    val order2 = Order(Map("wine" -> 3, "beer" -> 5))

    Order.combineOrder(order1, order2) shouldBe Order(Map("cola" -> 1, "wine" -> 5, "beer" -> 5))
  }

  it should "Add one order x number of times recursively" in {
    val order1 = Order(Map("wine" -> 1, "beer" -> 2))

    Order.times(order1, 5) shouldBe Order(Map("wine" -> 5, "beer" -> 10))
  }

  it should "Add one order x number of times using the semigroup's build in functionality" in {
    val order1 = Order(Map("wine" -> 1, "beer" -> 2))

    Order.combineN(order1, 5) shouldBe Order(Map("wine" -> 5, "beer" -> 10))
  }
}