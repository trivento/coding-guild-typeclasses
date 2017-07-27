package exercise0.answers

import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {
  import Domain._

  it should "encode and decode an object correctly" in {
    val person = Person("John")

    Decoder.decode(Encoder.encode(person)) should be(person)
  }
}
