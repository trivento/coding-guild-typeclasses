package exercise7.exercise

import java.time.{LocalDateTime, ZoneOffset}

import cats.functor.Contravariant
import cats.syntax.contravariant._
import exercise0.answer.Domain.StringEncoder

object Domain {

  /*
    * Base encoder for Long values, which can be used to derive a date-encoder (epoch based)
    */
  implicit def longEncoder: StringEncoder[Long] = (obj: Long) => obj.toString

  /*
    * Encoder is a contravariant functor. Prove it!
    */
  implicit val stringEncoderContravariant: Contravariant[StringEncoder] = ???

  /* Derive a date encoder from the base encoders above, using its contravariant functorial nature.
   */
  implicit def dateEncoder: StringEncoder[LocalDateTime] = {
    // You can use this function to encode a LocalDateTime as a Long
    val localDateTimeToEpoch: LocalDateTime ⇒ Long = _.toEpochSecond(ZoneOffset.UTC)
    // TODO:
    ???
  }
}
