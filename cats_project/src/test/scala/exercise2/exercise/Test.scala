package exercise2.exercise

import org.scalatest.{FlatSpec, Matchers}


class Test extends FlatSpec with Matchers {
  import Domain._

  it should "chain multiple orders" in {
    Order(Map()).add("cola" -> 1)
                .add("cola" -> 2)
                .add("wine" -> 1) shouldBe Order(Map("cola" -> 3, "wine" -> 1))
  }

  it should "allow persons to be added to a table" in {
    // We assume that people with the same name are the same person
    Table().addPerson(Person("Michael"))
           .addPerson(Person("John"))
           .addPerson(Person("Michelle"))
           .addPerson(Person("Michael")) shouldBe Table(Map(Person("Michael")  -> Order(),
                                                            Person("John")     -> Order(),
                                                            Person("Michelle") -> Order())
                                                       )
  }

  it should "allow persons make new orders" in {
    Table().addPerson(Person("Michael"))
           .addPerson(Person("John"))
           .order(Person("Michael") -> Order(Map("cola" -> 1)))
           .order(Person("John")    -> Order(Map("beer" -> 1)))
           .addPerson(Person("Anton"))
           .order(Person("Anton")   -> Order(Map("cola" -> 1)))
           .order(Person("Michael") -> Order(Map("cola" -> 1)))
           .order(Person("John")    -> Order(Map("wine" -> 1))) shouldBe
              Table(Map(
                Person("Michael") -> Order(Map("cola" -> 2)),
                Person("John")    -> Order(Map("beer" -> 1, "wine" -> 1)),
                Person("Anton")   -> Order(Map("cola" -> 1))
              ))
  }

  it should "allow persons make new orders, without registring themselves" in {
    Table().order(Person("Michael") -> Order(Map("cola" -> 1)))
           .order(Person("John")    -> Order(Map("beer" -> 1)))
           .order(Person("Anton")   -> Order(Map("cola" -> 1)))
           .order(Person("Michael") -> Order(Map("cola" -> 1)))
           .order(Person("John")    -> Order(Map("wine" -> 1))) shouldBe
              Table(Map(
                Person("Michael") -> Order(Map("cola" -> 2)),
                Person("John")    -> Order(Map("beer" -> 1, "wine" -> 1)),
                Person("Anton")   -> Order(Map("cola" -> 1))
              ))
  }
}