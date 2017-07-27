package validation

import cats.data.Validated._
import cats.data.{Validated, NonEmptyList => NEL}
import cats.instances.list._
import cats.syntax.traverse._
/**
  *
  */
class CatsValidationComposition extends CatsLocationValidation {

  // Constrain existing validating functions
  def validateUtrecht(name: String): Validated[NEL[String], City] =
    validateCity(name).ensure(NEL.of("is not Utrecht!"))(_ == Utrecht)

//  def validateNotBlacklisted(postcode: String) = validPostalcode(postcode). {
//    case Postcode("1000", "AB") ⇒ "Cannot accept 1000AB"
//    case Postcode("1000", "AC") ⇒ "Cannot accept 1000AC"
//  }

//  println("\nBLACKLISTED POSTCODES")
//  println(validateNotBlacklisted("1000AB"))
//  println(validateNotBlacklisted("1000AA"))


  // Use a conjunction/disjunction-like composition of validations
  def validatePrice(price: Int): Validated[NEL[String], Int] =
    if (price > 0) valid(price) else invalidNel(s"$price is a price that is zero or lower")
//
//  println("\nVALIDATING PRODUCT PRICES")
//  println(validatePrice(1) +++ validatePrice(-1) +++ validatePrice(2) +++ validatePrice(-2))
//  println(validatePrice(1) +|+ validatePrice(-1) +|+ validatePrice(2) +|+ validatePrice(-2))
//
  // Using traverse
  val validList1: Validated[NEL[String], List[Int]] = List(1, 2, 3).traverseU(validatePrice)
  val validList2: Validated[NEL[String], List[Int]] = List(4, 5, 6, 7).traverseU(validatePrice)
  val invalidList: Validated[NEL[String], List[Int]] = List(4, -5, -6, 7).traverseU(validatePrice)
//
//  println("\nVALIDATING PRODUCT PRICE-LISTS")
//  println(validList1 +++ invalidList +++ validList2)
//  println(validList1 +|+ invalidList +|+ validList2)
//  println(validList1 +++ invalidList +|+ validList2)
}


object CatsProductValidationApp extends CatsValidationComposition with App