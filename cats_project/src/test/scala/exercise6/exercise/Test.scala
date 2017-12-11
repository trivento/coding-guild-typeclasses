package exercise6.exercise

import cats.functor.Bifunctor
import org.scalatest.{FlatSpec, Matchers}

import scala.language.higherKinds

/**
  *
  */
class Test extends FlatSpec with Matchers {

  import Domain._
  import cats.syntax.bifunctor._

  def add1[BF[_, _]: Bifunctor](bf: BF[Int, String]): BF[Int, String] = bf.bimap(_ + 1, _ + "1")

  "Either" should "be bifunctorial" in {
    val left: Either[Int, String] = Left(1)
    val right: Either[Int, String] = Right("2")

    add1(left) shouldBe Left(2)
    add1(right) shouldBe Right("21")
  }

  "Tuple2" should "be bifunctorial" in {
    add1((1, "2")) shouldBe (2, "21")
  }

}
