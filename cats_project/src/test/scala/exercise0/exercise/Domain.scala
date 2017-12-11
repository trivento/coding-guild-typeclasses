package exercise0.exercise

import util.MissingImplicitsRuntimeException

object Domain extends MissingImplicitsRuntimeException {

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

  // TODO: Implement a String Encoder + Decoder for Person
  // (It suffices to just 'serialize' its name attribute)
}