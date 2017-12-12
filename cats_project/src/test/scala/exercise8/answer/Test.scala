package exercise8.answer

import org.scalatest.{FlatSpec, Matchers}

/**
  *
  */
class Test extends FlatSpec with Matchers {

  import Domain._
  import cats.syntax.profunctor._

  "Converter" should "be a valid Profunctor" in {
    implicit val coinConverter: Converter[String, Coin] = {
      case "BTC" => Bitcoin
      case "ETH" => Ethereum
      case "LTC" => Litecoin
    }

    val int2String: Int => String = {
      case 1 => "BTC"
      case 2 => "ETH"
      case 3 => "LTC"
    }

    val coin2Price: Coin => Price = {
      case Bitcoin  => Price(1696490)
      case Ethereum => Price(52817)
      case Litecoin => Price(24465)
    }

    implicit val int2PriceConverter: Converter[Int, Price] = coinConverter.dimap(int2String)(coin2Price)

    Converter[Int, Price].convert(1) shouldBe Price(1696490)
  }
}
