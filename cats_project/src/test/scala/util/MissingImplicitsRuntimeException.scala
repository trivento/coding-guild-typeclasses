package util

import exercise0.exercise.Domain.{Decoder, Encoder}

import scala.reflect.ClassTag

/**
  * Please ignore the existence of this trait. It is simply there to transform compile errors for missing implicits
  * into runtime errors, such that we can keep the tests the same between the answers - and exercise sections.
  */
trait MissingImplicitsRuntimeException {

  private def nameFromTag[T: ClassTag]: String = implicitly[ClassTag[T]].runtimeClass.getSimpleName

  implicit def missingDecoder[A: ClassTag, B: ClassTag]: Decoder[A, B] =
    throw new RuntimeException(s"No decoder found from ${nameFromTag[A]} to ${nameFromTag[B]}")

  implicit def missingEncoder[A: ClassTag, B: ClassTag]: Encoder[A, B] =
    throw new RuntimeException(s"No encoder found from ${nameFromTag[A]} to ${nameFromTag[B]}")
}