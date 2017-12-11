package exercise5.answer

import cats.Monoid
import cats.data.NonEmptyList._
import cats.data.Validated._
import cats.data.{Validated, NonEmptyList ⇒ NEL}
import cats.instances.int._
import cats.instances.list._
import cats.instances.map._
import cats.syntax.cartesian._
import cats.syntax.semigroup._
import cats.syntax.traverse._

object Domain {

  /* Our good old domain of some previous util */

  type Product = String
  type Price = Double
  type Number = Int
  type OrderRow = (Product, Number)

  val Catalog: Map[Product, Price] = Map(
    "cola" -> 2.75,
    "wine" -> 4.00,
    "beer" -> 3.25
  )

  case class Order(items: Map[Product, Number] = Map()) {
    def add(item: OrderRow): Order = Order(this.items |+| Map(item))

    // Implement using Fold
    def total(implicit monoid: Monoid[Price]): Double = items.foldLeft(monoid.empty){
      case (acc, (product, number)) => acc + monoid.combineN(Catalog(product), number)
    }
  }

  /* But now with validation on orders! */
  def noOrderShallBeLargerThan = 10
  def noOrderShallBeSmallerThan = 1

  trait OrderError
  case class NonExistingProduct(product: String) extends OrderError
  case class TooLargeOrder(product: String, count: Int, max: Int = noOrderShallBeLargerThan) extends OrderError
  case class TooSmallOrder(product: String, count: Int, min: Int = noOrderShallBeSmallerThan) extends OrderError


  /**
    * Validates that the given order-row amount does not exceed `noOrderShallBeLargerThan`
    */
  def validOrderMaxSize(orderRow: OrderRow): Validated[NEL[OrderError], OrderRow] =
    valid(orderRow).ensure(NEL.of(TooLargeOrder(orderRow._1, orderRow._2)))(noOrderShallBeLargerThan >= _._2)


  /**
    * Validates that the given order-row amount does not exceed `noOrderShallBeSmallerThan`
    */
  def validOrderMinSize(orderRow: OrderRow): Validated[NEL[OrderError], OrderRow] =
    valid(orderRow).ensure(NEL.of(TooSmallOrder(orderRow._1, orderRow._2)))(noOrderShallBeSmallerThan <= _._2)

  /**
    * Validates that the given product exists in the given catalog
    */
  def productInCatalog(catalog: Set[Product])(product: Product): Validated[NEL[OrderError], Product] =
    valid(product).ensure(NEL.of(NonExistingProduct(product)))(catalog.contains)


  def validateOrderRow(orderRow: OrderRow): Validated[NEL[OrderError], OrderRow] =
    validOrderMaxSize(orderRow) |@| validOrderMinSize(orderRow) |@| productInCatalog(Catalog.keySet)(orderRow._1) map ((_, _, _) => orderRow)

  /**
    * Validates an order by passing all the above rules over each order-row (and aggregating their results)
    */
  def validateOrder(order: Order): Validated[NEL[OrderError], Order] =
    order.items.map(validateOrderRow).toList.sequenceU.map(_.toMap).map(Order.apply)
}
