package typeclass

import generic.NamePrintingApp


/** IMPOSSIBLE TO CHANGE - FROM EXTERNAL LIB **/
case class Car(make: String, model: String) {
  def soundAlarm: String = "weoweoweo!!!"
}
/** ^^^^^^ IMPOSSIBLE TO CHANGE - FROM EXTERNAL LIB **/

case class Cat(name: String) {
  def scream: String = "miauw!!!"
}
trait Data {
  val bmw = Car("BMW", "730i")
  val garfield = Cat("Garfield")
}

//==============================================================================
//==============================================================================
//==============================================================================

object SimpleMethodsOverloading extends NamePrintingApp with Data {
  def kick(cat: Cat): String = cat.scream.toUpperCase
  def kick(car: Car): String = car.soundAlarm.toUpperCase

  println(s"Kicking the BMW: ${kick(bmw)}")
  println(s"Kicking Garfield: ${kick(garfield)}")
}

//==============================================================================
//==============================================================================
//==============================================================================

/*******************************************************************************
  * Above code doens't scale and isn't extendible by outside world
  * Let's make it more generic
  *
  * Abstract by:
  *   - define an interface with function for the type
  *   - define function that takes type plus function to perform operation
  *
  *****************************************************************************/
trait NoiseProducing[T] {
  def makeNoise(t: T): String
}
object Implicits {
  implicit val carNoise = new NoiseProducing[Car] {
    override def makeNoise(car: Car): String = car.soundAlarm
  }
  implicit val catNoise = new NoiseProducing[Cat] {
    override def makeNoise(cat: Cat): String = cat.scream
  }
}

object KickingMakesNoise extends NamePrintingApp with Data {
  import Implicits._

  // Give me a T and a function so that I can let the T make noise
  // This function is in NoiseProducing[T]
  def kickObject[T](obj: T)(implicit noiseProducing: NoiseProducing[T]): String =
    noiseProducing.makeNoise(obj).toUpperCase

  // Different syntax same result as above
  def kickObjectAltSyntax[T : NoiseProducing](obj: T): String =
    implicitly[NoiseProducing[T]].makeNoise(obj).toUpperCase

  println(s"Kicking the BMW: ${kickObject(bmw)(Implicits.carNoise)}")
  println(s"Kicking Garfield: ${kickObjectAltSyntax(garfield)}")
}

//==============================================================================
//==============================================================================
//==============================================================================

/*******************************************************************************
  * Expand example with Option to show the power
  *****************************************************************************/
object MaybeKickingSomething extends NamePrintingApp with Data {
  import Implicits._
  import KickingMakesNoise.kickObject

  val noCar: Option[Car] = None
  val myCar: Option[Car] = Some(bmw)

  implicit def optionNoise[T: NoiseProducing] = new NoiseProducing[Option[T]] {
    override def makeNoise(t: Option[T]): String = t match {
      case Some(o) => kickObject(o)
      case None => "......"
    }
  }

  println(s"Kicking the non-existing car: ${kickObject(noCar)}")
  println(s"Kicking Some(BMW): ${kickObject(myCar)}")
  println(s"Kicking Some(Garfield): ${kickObject(Option(garfield))}")
  println(s"Kicking the BMW again: ${kickObject(bmw)}")
}

//==============================================================================
//==============================================================================
//==============================================================================

object All extends NamePrintingApp {
  SimpleMethodsOverloading.main(Array.empty)
  KickingMakesNoise.main(Array.empty)
  MaybeKickingSomething.main(Array.empty)
}

