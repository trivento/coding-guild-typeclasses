package exercise7.answer

import java.time.{LocalDateTime, ZoneOffset}

import cats.Functor
import cats.functor.Contravariant
import cats.syntax.contravariant._
import cats.syntax.functor._
import exercise0.answer.Domain.{StringDecoder, StringEncoder}

object Domain {

  /*
    * Base encoder for Long values, which can be used to derive a date-encoder (epoch based)
    */
  implicit def longEncoder: StringEncoder[Long] = (obj: Long) => obj.toString

  /*
    * Encoder is a contravariant functor. Prove it!
    */
  implicit val stringEncoderContravariant: Contravariant[StringEncoder] = new Contravariant[StringEncoder] {
    override def contramap[A, B](fa: StringEncoder[A])(f: B => A): StringEncoder[B] = (obj: B) => fa.encode(f(obj))
  }

  /* Derive a date de/encoder from the base de/encoders above, using their (contravariant)functorial nature.
   */
  implicit def dateEncoder: StringEncoder[LocalDateTime] = {
    val localDateTimeToEpoch: LocalDateTime ⇒ Long = _.toEpochSecond(ZoneOffset.UTC)
    StringEncoder[Long].contramap(localDateTimeToEpoch)
  }
}
