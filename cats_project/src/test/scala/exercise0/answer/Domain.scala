package exercise0.answer

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

  // Named type-alias to construct a partially applied type
  // Alternatively, use anonymous/lambda syntax: ({type L[A] = Encoder[A, String]})#L
  // Alternatively, use kind-projector for less intimidating anonymous syntax: Encoder[?, String]
  type StringEncoder[A] = Encoder[A, String]
  object StringEncoder {
    def apply[A: StringEncoder]: StringEncoder[A] = implicitly[StringEncoder[A]]
  }

  type StringDecoder[A] = Decoder[A, String]
  object StringDecoder {
    def apply[A: StringDecoder]: StringDecoder[A] = implicitly[StringDecoder[A]]
  }

  implicit val personStringEncoder: StringEncoder[Person] = (obj: Person) => obj.name

  implicit val personStringDecoder: StringDecoder[Person] =  (obj: String) ⇒ Person(obj)

  case class Person(name: String)
}