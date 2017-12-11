package exercise4.exercise

import java.time.{LocalDateTime, ZoneOffset}

import exercise0.answer.Domain.StringDecoder
import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {

  import Domain._

  "LocalDateTime" should "allow decoding from epoch string" in {
    val now = LocalDateTime.now().withNano(0) // epoch encoding excludes nanoseconds
    val nowEncodedAsEpochString = now.toEpochSecond(ZoneOffset.UTC).toString
    val nowDecoded = StringDecoder[LocalDateTime].decode(nowEncodedAsEpochString)

    now shouldBe nowDecoded
  }
}
