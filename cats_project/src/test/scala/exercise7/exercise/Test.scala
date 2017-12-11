package exercise7.exercise

import java.time.LocalDateTime

import exercise0.answer.Domain.{StringDecoder, StringEncoder}
import exercise4.answer.Domain.dateDecoder
import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {

  import Domain._

  "LocalDateTime" should "allow en - and de-coding" in {
    val now = LocalDateTime.now().withNano(0) // epoch encoding excludes nanoseconds
    val nowEncoded = StringEncoder[LocalDateTime].encode(now)
    val nowDecoded = StringDecoder[LocalDateTime].decode(nowEncoded)

    now shouldBe nowDecoded
  }

  // Bonus:
  "Ordering" should "allow derivation into new Orderings" in {
    // TODO: Decide whether Ordering is co- or contravariant functorial
    // TODO: Use a basic Ordering for `(Int, String)` to derive an Ordering for `Subject`
    case class Subject(age: Int, name: String)

    // Use Functor/Contravariant from the base `Ordering[(Int, String)]`
    implicit val subjectOrder: Ordering[Subject] = ???

    val data = Seq(Subject(2, "abc"), Subject(3, "zyx"), Subject(3, "ghi"), Subject(1, "def"))
    // First sort on `age`, then on `name`
    data.sorted shouldBe Seq(Subject(1, "def"), Subject(2, "abc"), Subject(3, "ghi"), Subject(3, "zyx"))
  }
}
