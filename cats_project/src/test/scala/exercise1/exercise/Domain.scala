package exercise1.exercise

object Domain {
  type Product = String
  type Price = Double
  type Number = Int

  val Catalog: Map[Product, Price] = Map(
    "cola" -> 2.75,
    "wine" -> 4.00,
    "beer" -> 3.25
  )

  object Order {
    import cats.Semigroup
    import cats.implicits._

    implicit val orderSemigroup = new Semigroup[Order] {
      override def combine(x: Order, y: Order): Order = Order(x.items |+| y.items)
    }

    // Combine using the semigroup
    def combineOrder(o1: Order, o2: Order): Order = ???

    // Implement using recursion
    def times(o1: Order, times: Int): Order = ???

    def combineN(o1: Order, times: Int): Order = ???
  }
  case class Order(items: Map[Product, Number])
}
