package exercise8.exercise

import cats.functor.Profunctor

/**
  *
  */
object Domain {

  // Instead of having an Encoder and Decoder, we might as well have a single Converter typeclass
  // After all, they both share the A => B shape
  trait Converter[A, B] {
    def convert(a: A): B
  }
  object Converter {
    def apply[A, B](implicit converter: Converter[A, B]): Converter[A, B] = converter
    def from[A, B](f: A ⇒ B): Converter[A, B] = (a: A) => f(a)
  }

  implicit def identityConverter[A]: Converter[A, A] = Converter.from(identity)

  // Converter admits a Profunctor instance, because we have seen that Encoder and Decoder admit a Contravariant and
  // Functor respectively.
  implicit val converterProfunctor: Profunctor[Converter] = ???
}
