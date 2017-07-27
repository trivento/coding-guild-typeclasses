package exercise1.exercise0.answers

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

  trait StringEncoder[A] extends Encoder[A, String] {
    override def encode(obj: A): String
  }

  trait StringDecoder[A] extends Decoder[A, String] {
    override def decode(obj: String): A
  }

  implicit val personStringEncoder = new StringEncoder[Person] {
    override def encode(obj: Person): String = obj.name
  }

  implicit val personStringDecoder = new StringDecoder[Person] {
    override def decode(obj: String): Person = Person(obj)
  }

  case class Person(name: String)
}
