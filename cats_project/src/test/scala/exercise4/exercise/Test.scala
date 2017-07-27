package exercise4.exercise

import java.time.LocalDateTime

import exercise0.answers.Domain.{StringDecoder, StringEncoder}
import exercise4.answer.Domain._
import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {

  "LocalDateTimeEncoder" should "be co-functorial" in {
    val now = LocalDateTime.now().withNano(0) // epoch encoding excludes nanoseconds
    val nowEncoded = implicitly[StringEncoder[LocalDateTime]].encode(now)
    val nowDecoded = implicitly[StringDecoder[LocalDateTime]].decode(nowEncoded)

    now shouldBe nowDecoded
  }

}
