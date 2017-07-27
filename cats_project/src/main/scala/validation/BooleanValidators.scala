package validation

import java.time.LocalDate

import scala.util.matching.Regex

/**
  *
  */
trait BooleanValidators {

  // Generic boolean validations
  def isAlphabetic(s: String): Boolean = s.forall(_.isLetter)

  def matchesRegex(regex: Regex)(s: String): Boolean =
    regex.pattern.matcher(s).matches()

  def isContainedIn(container: List[String])(elem: String): Boolean =
    container.contains(elem)

  def hasAtLeastAge(minYears: Int)(date: LocalDate): Boolean =
    LocalDate.now().minusYears(minYears).isAfter(date)


  // domain specific validations
  // age validation
  def is18Years(birthDate: LocalDate): Boolean = hasAtLeastAge(18)(birthDate)

  // name validation (alphabetic string)
  def isName(s: String) = isAlphabetic(s)

  // postal code (regex validation)
  val postalCodeRegex: Regex = "^[0-9]{4}[A-Z]{2}$".r
  def isPostalCode(s: String) =
    matchesRegex(postalCodeRegex)(s)

  // country (enum validation)
  val countries: List[String] = List("Netherlands", "Belgium")
  def isLegalDrivingCountry(country: String): Boolean =
    isContainedIn(countries)(country)
}
object BooleanValidators extends BooleanValidators