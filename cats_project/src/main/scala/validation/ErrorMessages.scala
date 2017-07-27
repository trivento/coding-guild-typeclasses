package validation

/**
  *
  */
trait ErrorMessages {

  // error messages
  val notAValidName = "Not a valid name"
  val notAValidCountry = "Not a valid driving country"
  val notAValidAge = "Not a legal age"
  val notAPostalCode = "Not a postal code"
}
object ErrorMessages extends ErrorMessages
