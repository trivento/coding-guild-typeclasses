package exercise4.answer

import java.time.{LocalDateTime, ZoneOffset}

import cats.Functor
import cats.functor.Contravariant
import exercise0.answer.Domain.{StringDecoder, StringEncoder}
import cats.syntax.functor._
import cats.syntax.contravariant._

object Domain {

  /*
    * Base decoder for Long values, which can be used to derive a date-decoder (epoch based)
    */
  implicit def longDecoder: StringDecoder[Long] = (obj: String) => obj.toLong

  /*
    * Decoder is Functorial. Prove it!
    */
  implicit val stringDecoderFunctor: Functor[StringDecoder] = new Functor[StringDecoder] {
    override def map[A, B](fa: StringDecoder[A])(f: A => B): StringDecoder[B] = (obj: String) => f(fa.decode(obj))
  }

  /* Derive a date decoder from the base decoder above, using its functorial nature.
   */
  implicit def dateDecoder: StringDecoder[LocalDateTime] = {
    val dateFromEpoch: Long ⇒ LocalDateTime = LocalDateTime.ofEpochSecond(_, 0, ZoneOffset.UTC)
    StringDecoder[Long].map(dateFromEpoch)
  }

}
