package monoid

import cats.instances.int._
import cats.syntax.semigroup._
import cats.{Foldable, Monoid, Semigroup}
import generic.NamePrintingApp
import semigroup.Money

trait Data {
  val moneys = List(Money(10,40), Money(4,95), Money(8, 99))
  val marbles = List(10, 4, 5)
}

object Implicits {
  implicit val moneySemigroup = new Semigroup[Money] {
    override def combine(x: Money, y: Money): Money =
      Money(x.euros + y.euros + ((x.cents + y.cents) / 100), (x.cents + y.cents) % 100)
  }

}

/*******************************************************************************
  * We now know Semigroup
  * Let's sum up a list of Money and Int (marbles)
  *****************************************************************************/
object SummingExample extends NamePrintingApp with Data {
  import Implicits._

  def addAllMoneys(moneys: List[Money])(implicit semigroup: Semigroup[Money]): Money = {
    moneys.foldLeft(Money(0,0)) { case (sum, money) =>
      sum |+| money
    }
  }

  def addAllMarbles(marbles: List[Int])(implicit semigroup: Semigroup[Int]): Int = {
    marbles.foldLeft(0) { case (sum, marble) =>
      sum |+| marble
    }
  }

  val balance = addAllMoneys(moneys)
  val marbleSum = addAllMarbles(marbles)

  println(s"balance: $balance")
  println(s"my marbles: $marbleSum")

}

//==============================================================================
//==============================================================================
//==============================================================================

/*******************************************************************************
  * Ouch, replication again. Let's refactor
  *****************************************************************************/
object AbstractExample extends NamePrintingApp with Data {
  import Implicits._

  def fold[A: Semigroup](list: List[A], empty: A): A = {
    list.foldLeft(empty) { case (sum, elem) =>
      sum |+| elem
    }
  }

  val balance = fold(moneys, Money(0, 0))
  val marbleSum = fold(marbles, 0)

  println(s"balance: $balance")
  println(s"my marbles: $marbleSum")

}


//==============================================================================
//==============================================================================
//==============================================================================

/*******************************************************************************
  * Can even be simpler with Monoid:
  *
  * trait Semigroup[A] {
  *   def combine(x: A, y: A): A
  * }
  *
  * trait Monoid[A] extends Semigroup[A] {
  *   def empty: A
  * }
  *
  * So Monoid is a Semigroup with an empty definition. Or more formally:
  *
  * A associative binary operation and an identity element.
  *
  * Identity:
  *   - Monoid[T].combine(a: T, Monoid[T].empty) == a
  *   - Monoid[T].combine(Monoid[T].empty, a: T) == a
  *
  * That's exactly what we need!
  *****************************************************************************/
object MonoidExample extends NamePrintingApp with Data {
  implicit val moneyMonoid = new Monoid[Money] {
    override def combine(x: Money, y: Money): Money =
      Money(x.euros + y.euros + ((x.cents + y.cents) / 100), (x.cents + y.cents) % 100)

    override def empty: Money = Money(0, 0)
  }

  def fold[A: Monoid](list: List[A]): A = {
    list.foldLeft(Monoid[A].empty) { case (sum, elem) =>
      sum |+| elem
    }
  }

  val balance = fold(moneys)
  val marbleSum = fold(marbles)

  println(s"balance: $balance")
  println(s"my marbles: $marbleSum")
}

//==============================================================================
//==============================================================================
//==============================================================================

/*******************************************************************************
  * That look generic.
  * Does Cats have something Generic to fold a List?
  * Yes
  *
  * trait Foldable[F[_]] {
  *   def fold[A](fa: F[A])(implicit A: Monoid[A]): A =
  *     foldLeft(fa, A.empty) { (acc, a) =>
  *       A.combine(acc, a)
  *     }
  * }
  *
  *****************************************************************************/
object FoldExample extends NamePrintingApp with Data {
  implicit val moneyMonoid = new Monoid[Money] {
    override def combine(x: Money, y: Money): Money =
      Money(x.euros + y.euros + ((x.cents + y.cents) / 100), (x.cents + y.cents) % 100)

    override def empty: Money = Money(0, 0)
  }
  import cats.instances.list._

  val balance = Foldable[List].fold(moneys)
  val marbleSum = Foldable[List].fold(marbles)


  println(s"balance: $balance")
  println(s"my marbles: $marbleSum")

}

//==============================================================================
//==============================================================================
//==============================================================================
/**
  * Just for running all examples
  */
object All extends NamePrintingApp {
  SummingExample.main(Array.empty)
  AbstractExample.main(Array.empty)
  MonoidExample.main(Array.empty)
  FoldExample.main(Array.empty)
}