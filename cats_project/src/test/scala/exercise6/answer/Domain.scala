package exercise6.answer

import cats.functor.Bifunctor

/**
  *
  */
object Domain {

  implicit lazy val eitherBifunctor: Bifunctor[Either] = new Bifunctor[Either] {
    override def bimap[A, B, C, D](fab: Either[A, B])(f: A ⇒ C, g: B ⇒ D): Either[C, D] =
      fab.map(g).left.map(f)
  }

  implicit lazy val tupleBifunctor: Bifunctor[Tuple2] = new Bifunctor[Tuple2] {
    override def bimap[A, B, C, D](fab: (A, B))(f: A ⇒ C, g: B ⇒ D): (C, D) =
      (f(fab._1), g(fab._2))
  }
}
