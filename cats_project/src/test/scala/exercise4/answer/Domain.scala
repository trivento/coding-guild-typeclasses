package exercise4.answer

import java.time.{LocalDateTime, ZoneOffset}

import cats.Functor
import cats.functor.Contravariant
import exercise0.answers.Domain.{StringDecoder, StringEncoder}
import cats.syntax.functor._
import cats.syntax.contravariant._

object Domain {

  /*
    * Base de/encoder for Long values, which can be used to derive the lower date-de/encoders (epoch based)
    */
  implicit def longEncoder: StringEncoder[Long] = new StringEncoder[Long] {
    override def encode(obj: Long): String = obj.toString
  }
  implicit def longDecoder: StringDecoder[Long] = new StringDecoder[Long] {
    override def decode(obj: String): Long = obj.toInt
  }

  /*
    * Decoder is Functorial, Encoder is Co(ntra-variant)functorial. Prove it!
    */
  implicit val stringDecoderFunctor: Functor[StringDecoder] = new Functor[StringDecoder] {
    override def map[A, B](fa: StringDecoder[A])(f: A => B): StringDecoder[B] = new StringDecoder[B] {
      override def decode(obj: String): B = f(fa.decode(obj))
    }
  }

  implicit val stringEncoderCofunctor: Contravariant[StringEncoder] = new Contravariant[StringEncoder] {
    override def contramap[A, B](fa: StringEncoder[A])(f: B => A): StringEncoder[B] = new StringEncoder[B] {
      override def encode(obj: B): String = fa.encode(f(obj))
    }
  }

  /* Derive a date de/encoder from the base de/encoders above, using their (contravariant)functorial nature
   */
  implicit def dateEncoder: StringEncoder[LocalDateTime] =
    longEncoder.contramap[LocalDateTime](b => b.toEpochSecond(ZoneOffset.UTC))

  implicit def dateDecoder: StringDecoder[LocalDateTime] =
    longDecoder.map(epoch => LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC))

}
