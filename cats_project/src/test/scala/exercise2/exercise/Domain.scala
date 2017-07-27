package exercise2.exercise

object Domain {
  import cats.Semigroup
  import cats.implicits._

  type Product = String
  type Price = Double
  type Number = Int

  val Catalog: Map[Product, Price] = Map(
    "cola" -> 2.75,
    "wine" -> 4.00,
    "beer" -> 3.25
  )

  case class Order(items: Map[Product, Number] = Map()) {
    def add(item: (Product, Number)): Order = ???
  }

  case class Person(name: String)

  case class Table(personOrders: Map[Person, Order] = Map()) {
    def addPerson(person: Person): Table = ???
    def order(personOrder: (Person, Order)): Table = ???
  }
}
