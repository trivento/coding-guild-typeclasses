package exercise0.exercise

object Domain {

  object Encoder {
    def encode[A, B](obj: A)(implicit encoder: Encoder[A, B]): B = encoder.encode(obj)
  }

  object Decoder {
    def decode[A, B](obj: B)(implicit decoder: Decoder[A, B]): A = decoder.decode(obj)
  }

  trait Encoder[A, B] {
    def encode(obj: A): B
  }

  trait Decoder[A, B] {
    def decode(obj: B): A
  }

  case class Person(name: String)
}
