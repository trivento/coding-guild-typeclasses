package exercise4.exercise

import java.time.{LocalDateTime, ZoneOffset}

import cats.Functor
import exercise0.answer.Domain.StringDecoder

object Domain {
  /*
    * Base decoder for Long values, which can be used to derive a date-decoder (epoch based)
    */
  implicit def longDecoder: StringDecoder[Long] = (obj: String) => obj.toLong

  /*
    * Decoder is Functorial. Prove it!
    */
  implicit val stringDecoderFunctor: Functor[StringDecoder] = ???

  /* Derive a date decoder from the base decoder above, using its functorial nature.
   */
  implicit def dateDecoder: StringDecoder[LocalDateTime] = {
    // Use the following function to get a date from an epoch
    val dateFromEpoch: Long ⇒ LocalDateTime = LocalDateTime.ofEpochSecond(_, 0, ZoneOffset.UTC)
    // TODO:
    ???
  }
}
