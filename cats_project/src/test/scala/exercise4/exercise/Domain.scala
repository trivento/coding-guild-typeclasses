package exercise4.exercise

import java.time.LocalDateTime

import cats.Functor
import cats.functor.Contravariant
import exercise0.answers.Domain.{StringDecoder, StringEncoder}

object Domain {

  /*
  Base de/encoder for Long values, which can be used to derive the lower date-de/encoders (epoch based)
    */
  implicit def longEncoder: StringEncoder[Long] = new StringEncoder[Long] {
    override def encode(obj: Long): String = obj.toString
  }

  implicit def longDecoder: StringDecoder[Long] = new StringDecoder[Long] {
    override def decode(obj: String): Long = obj.toInt
  }

  /*
  Decoder is Functorial, Encoder is Co(ntra-variant)functorial. Prove it!
    */
  implicit val stringDecoderFunctor: Functor[StringDecoder] = ???

  implicit val stringEncoderCofunctor: Contravariant[StringEncoder] = ???

  /*
  Derive a date de/encoder from the base de/encoders above, using their (contravariant)functorial nature
   */
  implicit def dateEncoder: StringEncoder[LocalDateTime] = ???

  implicit def dateDecoder: StringDecoder[LocalDateTime] = ???
}
