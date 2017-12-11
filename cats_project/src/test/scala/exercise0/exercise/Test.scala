package exercise0.exercise

import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {
  import Domain._

  it should "encode and decode an object correctly" in {
    val john = Person("John")
    val encodedThenDecoded: Person = Decoder.decode(Encoder.encode(john))
    encodedThenDecoded shouldBe john
  }
}
