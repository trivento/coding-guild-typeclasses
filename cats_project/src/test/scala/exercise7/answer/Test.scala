package exercise7.answer

import java.time.LocalDateTime

import cats.functor.Contravariant
import exercise0.answer.Domain.{StringDecoder, StringEncoder}
import exercise4.answer.Domain.dateDecoder
import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {

  import Domain._

  "LocalDateTime" should "(now) allow en - and de-coding" in {
    val now = LocalDateTime.now().withNano(0) // epoch encoding excludes nanoseconds
    val nowEncoded = StringEncoder[LocalDateTime].encode(now)
    val nowDecoded = StringDecoder[LocalDateTime].decode(nowEncoded)

    now shouldBe nowDecoded
  }

  // Bonus:
  "Ordering" should "allow transformation into new Orderings" in {
    case class Subject(age: Int, name: String)

    import cats.syntax.contravariant._
    implicit def orderContravariant: Contravariant[Ordering] = new Contravariant[Ordering] {
      override def contramap[A, B](fa: Ordering[A])(f: B ⇒ A): Ordering[B] = fa.on(f)
    }

    // NOTE: contramap does no real work here, Order defines `on`, which we could use as well directly
    implicit val subjectOrder: Ordering[Subject] =
      Ordering[(Int, String)].contramap(subject ⇒ (subject.age, subject.name))

    val data = Seq(Subject(2, "abc"), Subject(3, "zyx"), Subject(3, "ghi"), Subject(1, "def"))
    data.sorted shouldBe Seq(Subject(1, "def"), Subject(2, "abc"), Subject(3, "ghi"), Subject(3, "zyx"))
  }
}
