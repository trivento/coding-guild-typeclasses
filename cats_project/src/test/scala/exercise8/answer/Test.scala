package exercise8.answer

import java.sql.{Date ⇒ SqlDate}
import java.util.{Date ⇒ UtilDate}

import org.scalatest.{FlatSpec, Matchers}

/**
  *
  */
class Test extends FlatSpec with Matchers {

  import Domain._
  import cats.syntax.profunctor._

  "Converter" should "be a valid Profunctor" in {
    val utilToSqlDate: Converter[UtilDate, SqlDate] =
      Converter[Long, Long].dimap[UtilDate, SqlDate](_.getTime)(new SqlDate(_))

    val utilDate = new UtilDate(2000, 1, 1)
    val sqlDate = new SqlDate(2000, 1, 1)
    utilToSqlDate.convert(utilDate) shouldBe sqlDate
  }
}
