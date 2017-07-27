package validation

import BooleanValidators._
import TestData._
import ErrorMessages._

/**
  *
  */
object EitherBasedValidation extends App {

  def isLegalDrivingPersonEitherSimple(person: Person): Either[String, Person] = {
    import person._
    val condition = isName(name) && isPostalCode(postalCode) && is18Years(birthDate) && isLegalDrivingCountry(country)
    Either.cond(condition, person, "Not a valid driving person")
  }

  println("\nSIMPLE")
  println(isLegalDrivingPersonEitherSimple(validPerson))
  println(isLegalDrivingPersonEitherSimple(partiallyValidPerson))
  println(isLegalDrivingPersonEitherSimple(completelyInvalidPerson))


  def isLegalDrivingPersonEitherDetailed(person: Person): Either[String, Person] = {
    import person._
    if(isName(name)) {
      if(isPostalCode(postalCode)) {
        if(is18Years(birthDate)) {
          if(isLegalDrivingCountry(country)) {
            Right(person)
          } else
            Left(notAValidCountry)
        } else
          Left(notAValidAge)
      } else
        Left(notAPostalCode)
    } else
      Left(notAValidName)
  }

  println("\nDETAILED")
  println(isLegalDrivingPersonEitherDetailed(validPerson))
  println(isLegalDrivingPersonEitherDetailed(partiallyValidPerson))
  println(isLegalDrivingPersonEitherDetailed(completelyInvalidPerson))


  def isLegalDrivingPersonEitherMonadic(person: Person): Either[String, Person] = {
    import person._
    for {
      _ ← Either.cond(isName(name), person, notAValidName)
      _ ← Either.cond(isPostalCode(postalCode), person, notAPostalCode)
      _ ← Either.cond(is18Years(birthDate), person, notAValidAge)
      _ ← Either.cond(isLegalDrivingCountry(country), person, notAValidCountry)
    } yield person
  }

  println("\nMONADIC")
  println(isLegalDrivingPersonEitherMonadic(validPerson))
  println(isLegalDrivingPersonEitherMonadic(partiallyValidPerson))
  println(isLegalDrivingPersonEitherMonadic(completelyInvalidPerson))
}
