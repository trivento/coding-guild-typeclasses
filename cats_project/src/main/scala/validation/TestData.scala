package validation

import java.time.LocalDate

/**
  *
  */
trait TestData {

  val validPerson = Person("Jantje", "1234AB", LocalDate.now().minusYears(30), "Netherlands")
  val partiallyValidPerson = Person("Jantje2", "1234AB", LocalDate.now().minusYears(30), "Netherlands")
  val completelyInvalidPerson = Person("Jan9", "1234ABC", LocalDate.now(), "Germany")
}

object TestData extends TestData
