package exercise6.exercise

import cats.functor.Bifunctor

/**
  *
  */
object Domain {

  implicit lazy val eitherBifunctor: Bifunctor[Either] = ???

  implicit lazy val tupleBifunctor: Bifunctor[Tuple2] = ???
}
