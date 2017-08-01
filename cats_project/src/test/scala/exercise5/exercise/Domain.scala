package exercise5.exercise

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

  /* Our good old domain of some previous exercises */

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
  def validOrderMaxSize(orderRow: OrderRow): Validated[NEL[OrderError], OrderRow] = ???


  /**
    * Validates that the given order-row amount does not exceed `noOrderShallBeSmallerThan`
    */
  def validOrderMinSize(orderRow: OrderRow): Validated[NEL[OrderError], OrderRow] = ???

  /**
    * Validates that the given product exists in the given catalog
    */
  def productInCatalog(catalog: Set[Product])(product: Product): Validated[NEL[OrderError], Product] = ???


  def validateOrderRow(orderRow: OrderRow): Validated[NEL[OrderError], OrderRow] = ???

  /**
    * Validates an order by passing all the above rules over each order-row (and aggregating their results)
    */
  def validateOrder(order: Order): Validated[NEL[OrderError], Order] = ???
}
