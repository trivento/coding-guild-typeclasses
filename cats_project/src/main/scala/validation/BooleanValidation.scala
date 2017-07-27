package validation

import validation.BooleanValidators._
import validation.TestData._

/**
  *
  */
object BooleanValidation extends App {

  def isLegalDrivingPerson(person: Person): Boolean = {
    import person._
    isName(name) && isPostalCode(postalCode) && is18Years(birthDate) && isLegalDrivingCountry(country)
  }

  println(isLegalDrivingPerson(validPerson))
  println(isLegalDrivingPerson(partiallyValidPerson))
  println(isLegalDrivingPerson(completelyInvalidPerson))
}
