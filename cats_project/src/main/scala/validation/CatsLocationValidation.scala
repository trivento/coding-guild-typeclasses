package validation

import cats.data.{Validated, NonEmptyList => NEL}
import cats.syntax.cartesian._
import validation.ErrorMessages._
import cats.data.Validated._
/**
  *
  */
class CatsLocationValidation {

  case class RawLocation(cityname: String, postcode: String)

  sealed trait City
  case object Amsterdam extends City
  case object Utrecht extends City

  case class Postcode(region: String, street: String)

  case class Location(name: City, postcode: Postcode)


  def validateCity(name: String): Validated[NEL[String], City] = name match {
    case "Amsterdam" ⇒ valid(Amsterdam)
    case "Utrecht" ⇒ valid(Utrecht)
    case other ⇒ invalidNel(s"$other is not a valid city")
  }

  val postalCodeMatch = "^([0-9]{4})([A-Z]{2})$".r
  def validPostalcode(code: String): Validated[NEL[String], Postcode] = code match {
    case postalCodeMatch(region, city) ⇒ valid(Postcode(region, city))
    case _ ⇒ invalidNel(notAPostalCode)
  }

  def validateLocation(input: RawLocation): Validated[NEL[String], Location] =
    (validateCity(input.cityname) |@| validPostalcode(input.postcode)) map ((ci, pc) ⇒ Location(ci, pc))

  println(validateLocation(RawLocation("Amsterdam", "1234AB")))
  println(validateLocation(RawLocation("Amsterdam", "1234ABC")))
  println(validateLocation(RawLocation("Rotterdam", "1234AB")))
  println(validateLocation(RawLocation("Rotterdam", "1234ABC")))
}

object CatsLocationValidationApp extends CatsLocationValidation with App