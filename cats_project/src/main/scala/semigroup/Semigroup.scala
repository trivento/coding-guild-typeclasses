package semigroup

import generic.NamePrintingApp
import semigroup.CatsGenericAddBalanceExample.{add, balance, balances, marbles, salaries, salary, won}

/**
  * DOMAIN
  */
case class Money(euros: Int, cents: Int)

/**
  * Data we use in examples below
  */
trait Data {

  val balance: Money = Money(987, 85)
  val salary: Money = Money(834, 78)

  // How many marbles has each Person
  val marbles: Map[String, Int] = Map(
    "John" -> 6,
    "Sara" -> 9
  )

  // How many marbles did each person win
  val won: Map[String, Int] = Map(
    "Sara" -> 2,
    "Mo" -> 5
  )

  // Current balance of each person
  val balances: Map[String, Money] = Map(
    "John" -> Money(987, 85),
    "Sara" -> Money(1234, 98)
  )

  // Salary of each person
  val salaries: Map[String, Money] = Map(
    "Sara" -> Money(200, 90),
    "Mo" -> Money(4987,43)
  )

}

//========================================================================================
//========================================================================================
//========================================================================================

/**
  * We want to add 2 money objects
  * We want to add 2 maps with money in it for each person
  * We want to add 2 maps with #marbles in it for each person
  */
object BalanceExample extends NamePrintingApp with Data {

  // Function to add 2 money objects to each other
  def add(money: Money, other: Money): Money =
    Money(money.euros + other.euros + ((money.cents + other.cents) / 100), (money.cents + other.cents) % 100)

  // Due to type erasure we need 2 functions for the maps
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

  println(s"1. Add 2 money objects ${add(balance, salary)}")
  println(s"1. Payday adding Maps${addMoneyMap(balances, salaries)}")
  println(s"1. Game of marbles ${addMarbleMap(marbles, won)}")
}

//========================================================================================
//========================================================================================
//========================================================================================

/**
  * Adding maps seems to be generic. Let's abstract that away
  */
object GenericAddBalanceExample extends NamePrintingApp with Data  {

  def add(money: Money, other: Money): Money =
    Money(money.euros + other.euros + ((money.cents + other.cents) / 100), (money.cents + other.cents) % 100)

  trait Addable[T] {
    def add(a: T, b: T): T
  }

  // now we can use the same name, because type erasure is no longer playing parts
  // Add two maps to each other
  def add[X, Y](balances: Map[X, Y], addMap: Map[X, Y])(addable: Addable[Y]): Map[X, Y] = {
    addMap.foldLeft(balances){
      case (acc, (name, plus)) =>
        acc + (name -> acc.get(name).map(addable.add(_, plus)).getOrElse(plus))
    }
  }

  println(s"2. Add 2 money objects ${add(balance, salary)}")
  // With explicit Addable for money
  println(s"2. Payday adding Maps${add(balances, salaries){(a: Money, b: Money) => add(a,b)}}")
  // With explicit Addable for Int
  println(s"2. Game of marbles ${add(marbles, won){(a: Int, b: Int) => a + b}}")
}

//========================================================================================
//========================================================================================
//========================================================================================

/**
  * Let's use implicits for the Addable
  */
object ImplicitsGenericAddBalanceExample extends NamePrintingApp with Data  {
  trait Addable[T] {
    def add(a: T, b: T): T
  }

  implicit val intAddable = new Addable[Int] {
    override def add(a: Int, b: Int): Int = a + b
  }

  // BONUS: Nice 2.12 trick for implementing single method interface
  implicit val moneyAddable: Addable[Money] = { (money, other) =>
      Money(money.euros + other.euros + ((money.cents + other.cents) / 100), (money.cents + other.cents) % 100)
  }

  implicit def mapAddable[K, V: Addable] = new Addable[Map[K,V]] {
    override def add(a: Map[K, V], b: Map[K, V]): Map[K, V] =
      a.foldLeft(b){
        case (acc, (key, value)) =>
          acc + (key -> acc.get(key).map(implicitly[Addable[V]].add(_, value)).getOrElse(value))
      }
  }

  /**
    * Generic add function which can add anything as long as an Addable of it
    * is in implicit scope (or explicitly passed)
    */
  def add[A: Addable](a: A, b: A): A = implicitly[Addable[A]].add(a, b)

  println(s"3. Add 2 money objects ${add(balance, salary)}")
  println(s"3. Payday adding Maps${add(balances, salaries)}")
  println(s"3. Game of marbles ${add(marbles, won)}")
}

//========================================================================================
//========================================================================================
//========================================================================================

/**
  * Let's use FP concepts with Cats
  */


/******************************************************************************
  * What did we do so far?
  *
  *  - Defined an Addable interface
  *  - Created instances of the Addable interface
  *
  * In FP we have SemiGroup
  * Semigroup defines an associative binary operation
  *
  * trait Semigroup[A] {
  *   def combine(x: A, y: A): A
  * }
  *
  ******************************************************************************/
object CatsGenericAddBalanceExample extends NamePrintingApp with Data  {
  import cats.Semigroup

  /******************************************************************************
    * Let's define a Semigroup to combine (add) 2 money objects
    ******************************************************************************/
  implicit val moneySemigroup = new Semigroup[Money] {
    override def combine(x: Money, y: Money): Money =
    Money(x.euros + y.euros + ((x.cents + y.cents) / 100), (x.cents + y.cents) % 100)
  }

  /******************************************************************************
    * There are definitions for Int for free!
    ******************************************************************************/
  import cats.instances.int._
  Semigroup[Int].combine(1, 2)

  /******************************************************************************
    * And also for Map!
    ******************************************************************************/
  import cats.instances.map._

  // before: def add[A: Addable](a: A, b: A): A = implicitly[Addable[A]].add(a, b)
  def add[A: Semigroup](a: A, b: A): A = implicitly[Semigroup[A]].combine(a, b)

  println(s"4. Add 2 money objects ${add(balance, salary)}")
  println(s"4. Payday adding Maps${add(balances, salaries)}")
  println(s"4. Game of marbles ${add(marbles, won)}")

}

//========================================================================================
//========================================================================================
//========================================================================================

/**
  * It is also possible to use symbols
  * |+| is something like:
  *
  * implicit class SemigroupOps[A: Semigroup](lhs: A) {
  *   def combine(rhs: A): A = implicitly[Semigroup[A]].combine(a, b)
  *   def |+|(rhs: A): A = combine(rhs)
  * }
  *
  * So combine does the same as our add function
  */
object SyntaxCatsGenericAddBalanceExample extends NamePrintingApp with Data {
  import cats.Semigroup
  import cats.instances.int._
  import cats.instances.map._
  import cats.syntax.semigroup._

  implicit val moneySemigroup = new Semigroup[Money] {
    override def combine(x: Money, y: Money): Money =
      Money(x.euros + y.euros + ((x.cents + y.cents) / 100), (x.cents + y.cents) % 100)
  }

  println(s"4. Add 2 money objects ${balance.combine(salary)}")
  println(s"4. Payday adding Maps${balances |+| salaries}")
  println(s"4. Game of marbles ${marbles |+| won}")

}


//========================================================================================
//========================================================================================
//========================================================================================
/**
  * Just for running all examples
  */
object All extends NamePrintingApp {
  BalanceExample.main(Array.empty)
  GenericAddBalanceExample.main(Array.empty)
  ImplicitsGenericAddBalanceExample.main(Array.empty)
  CatsGenericAddBalanceExample.main(Array.empty)
  SyntaxCatsGenericAddBalanceExample.main(Array.empty)
}

