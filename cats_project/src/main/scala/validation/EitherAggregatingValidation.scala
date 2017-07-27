package validation

import validation.BooleanValidators._
import validation.ErrorMessages._
import validation.TestData._

/**
  *
  */
object EitherAggregatingValidation extends App {

  def isLegalDrivingPersonEitherAggregating(person: Person): Either[List[String], Person] = {
    def error(flag: Boolean)(error: ⇒ String): List[String] = if (!flag) List(error) else Nil

    import person._
    val errors =
      error(isName(name))(notAValidName) ++
        error(isPostalCode(postalCode))(notAPostalCode) ++
        error(is18Years(birthDate))(notAValidAge) ++
        error(isLegalDrivingCountry(country))(notAValidCountry)

    Either.cond(errors.isEmpty, person, errors)
  }

  println(isLegalDrivingPersonEitherAggregating(validPerson))
  println(isLegalDrivingPersonEitherAggregating(partiallyValidPerson))
  println(isLegalDrivingPersonEitherAggregating(completelyInvalidPerson))

}
