package validation

import java.time.LocalDate

import cats.data.Validated._
import cats.data.{Validated, NonEmptyList => NEL}
import cats.syntax.cartesian._
import validation.BooleanValidators._
import validation.ErrorMessages._
import validation.TestData._

/**
  *
  */
object CatsPersonValidation extends App {

  // Validation
  def isValidName(s: String): Validated[NEL[String], String] =
    if(isAlphabetic(s)) valid(s) else invalidNel(notAValidName)

  def isValidPostalCode(s: String): Validated[NEL[String], String] =
    if(isPostalCode(s)) valid(s) else invalidNel(notAPostalCode)

  def isValidLegalDrivingCountry(country: String): Validated[NEL[String], String] =
    if(isAlphabetic(country)) valid(country) else invalidNel(notAValidCountry)

  def isValidAge(birthDate: LocalDate): Validated[NEL[String], LocalDate] =
    if(is18Years(birthDate)) valid(birthDate) else invalidNel(notAValidAge)


  def validatePerson(person: Person): Validated[NEL[String], Person] = {
    (isValidName(person.name) |@|
      isValidPostalCode(person.postalCode) |@|
      isValidLegalDrivingCountry(person.country) |@|
      isValidAge(person.birthDate)) map ((_, _, _, _) ⇒ person)
  }

  println(validatePerson(validPerson))
  println(validatePerson(partiallyValidPerson))
  println(validatePerson(completelyInvalidPerson))
}
