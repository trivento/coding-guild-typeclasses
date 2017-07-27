package exercise1.answer

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
    def combineOrder(o1: Order, o2: Order)(implicit semigroup: Semigroup[Order]): Order = semigroup.combine(o1, o2)

    // Implement using recursion
    def times(o1: Order, times: Int)(implicit semigroup: Semigroup[Order]): Order = {

      def timesInternal(acc: Order, times: Int): Order = {
        require(times > 0)
        if (times == 1) acc
        else timesInternal(semigroup.combine(acc, o1), times - 1)
      }

      timesInternal(o1, times)
    }
    def combineN(o1: Order, times: Int)(implicit semigroup: Semigroup[Order]): Order = semigroup.combineN(o1, times)
  }
  case class Order(items: Map[Product, Number])
}
