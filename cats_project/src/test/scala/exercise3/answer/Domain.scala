package exercise3.answer

import cats.implicits._
import cats.{Monoid, Semigroup}

object Domain {
  type Product = String
  type Price = Double
  type Number = Int

  val Catalog: Map[Product, Price] = Map(
    "cola" -> 2.75,
    "wine" -> 4.00,
    "beer" -> 3.25
  )

  case class Order(items: Map[Product, Number] = Map()) {
    def add(item: (Product, Number)): Order = Order(this.items |+| Map(item))

    // Implement using Fold
    def total(implicit monoid: Monoid[Price]): Double = items.foldLeft(monoid.empty){
      case (acc, (product, number)) => acc + monoid.combineN(Catalog(product), number)
    }
  }

  case class Person(name: String)

  implicit val personSemigroup = new Semigroup[Person] {
    override def combine(x: Person, y: Person): Person = Person(x.name |+| y.name)
  }

  implicit val orderSemigroup = new Semigroup[Order] {
    override def combine(x: Order, y: Order): Order = Order(x.items |+| y.items)
  }

  case class Table(personOrders: Map[Person, Order] = Map()) {
    def addPerson(person: Person) = Table(personOrders |+| Map(person -> Order()))
    def order(personOrder: (Person, Order)) = Table(personOrders |+| Map(personOrder))

    def total: Price = personOrders.map {
      case (_, order) => order.total
    }.sum

    def combine(other: Table): Table = Table(personOrders |+| other.personOrders)
  }
}
