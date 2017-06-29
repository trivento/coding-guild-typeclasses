package semigroup

import cats.Semigroup
import cats.instances.map._
import cats.instances.int._

trait Data {
  case class Money(euros: Int, cents: Int)

  val balance: Money = Money(987, 85)
  val salary: Money = Money(834, 78)

  val marbles: Map[String, Int] = Map(
    "John" -> 6,
    "Sara" -> 9
  )

  val won: Map[String, Int] = Map(
    "Sara" -> 2,
    "Mo" -> 5
  )

  val balances: Map[String, Money] = Map(
    "John" -> Money(987, 85),
    "Sara" -> Money(1234, 98)
  )

  val salaries: Map[String, Money] = Map(
    "Sara" -> Money(200, 90),
    "Mo" -> Money(4987,43)
  )

}


object SemigroupEx1 extends App with Data {

  def add(money: Money, other: Money): Money =
    Money(money.euros + other.euros + ((money.cents + other.cents) / 100), (money.cents + other.cents) % 100)

  def addMoneyMap(balances: Map[String, Money], salaries: Map[String, Money]): Map[String, Money] = {
    balances.foldLeft(salaries){
      case (acc, (name, money)) =>
        acc + (name -> acc.get(name).map(add(_, money)).getOrElse(money))
    }
  }
  def addMarbleMap(balances: Map[String, Int], salaries: Map[String, Int]): Map[String, Int] = {
    balances.foldLeft(salaries){
      case (acc, (name, nrOfMarbles)) =>
        acc + (name -> acc.get(name).map(_ + nrOfMarbles).getOrElse(nrOfMarbles))
    }
  }

  println(s"1. New balance ${add(balance, salary)}")
  println(s"1. New balance ${addMoneyMap(balances, salaries)}")
  println(s"1. New marbles ${addMarbleMap(marbles, won)}")
}

object SemigroupEx2 extends App with Data  {

  def add(money: Money, other: Money): Money =
    Money(money.euros + other.euros + ((money.cents + other.cents) / 100), (money.cents + other.cents) % 100)

  trait Addable[T] {
    def add(a: T, b: T): T
  }

  def add[X, Y](balances: Map[X, Y], salaries: Map[X, Y])(addable: Addable[Y]): Map[X, Y] = {
    salaries.foldLeft(balances){
      case (acc, (name, money)) =>
        acc + (name -> acc.get(name).map(addable.add(_, money)).getOrElse(money))
    }
  }

  println(s"2. New balance ${add(balance, salary)}")
  println(s"2. New balance ${add(balances, salaries){case (a: Money, b: Money) => add(a,b)}}")
  println(s"2. New marbles ${add(marbles, won){case (a: Int, b: Int) => a + b}}")
}

object SemigroupEx3 extends App with Data  {
  trait Addable[T] {
    def add(a: T, b: T): T
  }

  implicit val intAddable = new Addable[Int] {
    override def add(a: Int, b: Int): Int = a + b
  }
  implicit val moneyAddable = new Addable[Money] {
    override def add(money: Money, other: Money): SemigroupEx3.Money =
      Money(money.euros + other.euros + ((money.cents + other.cents) / 100), (money.cents + other.cents) % 100)
  }
  implicit def mapAddable[K, V: Addable] = new Addable[Map[K,V]] {
    override def add(a: Map[K, V], b: Map[K, V]): Map[K, V] =
      a.foldLeft(b){
        case (acc, (key, value)) =>
          acc + (key -> acc.get(key).map(implicitly[Addable[V]].add(_, value)).getOrElse(value))
      }
  }

  println(s"3. New balance ${implicitly[Addable[Money]].add(balance, salary)}")
  println(s"3. New balance ${implicitly[Addable[Map[String, Money]]].add(balances, salaries)}")
  println(s"3. New marbles ${implicitly[Addable[Map[String, Int]]].add(marbles, won)}")
}

object SemigroupEx4 extends App with Data  {

  implicit val semigroup = new Semigroup[Money] {
    override def combine(x: Money, y: Money): Money =
      Money(x.euros + y.euros + ((x.cents + y.cents) / 100), (x.cents + y.cents) % 100)
  }
  Semigroup[Int].combine(1, 2)

  println(s"4. New balance ${Semigroup[Money].combine(balance, salary)}")
  println(s"4. New balance ${Semigroup[Map[String,Money]].combine(balances, salaries)}")
  println(s"4. New balance ${Semigroup[Map[String,Int]].combine(marbles, won)}")

}
/*
     New balance Money(1822,63)
     New balance Map(John -> Money(987,85), Mo -> Money(4987,43), Sara -> Money(1435,88))
[SG] New balance Money(1822,63)
[SG] New balance Map(Sara -> Money(1435,88), Mo -> Money(4987,43), John -> Money(987,85))
 */