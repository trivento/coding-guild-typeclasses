package typeclass


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

object Main extends App with Data {
  def kick(cat: Cat): String = cat.scream.toUpperCase
  def kick(car: Car): String = car.soundAlarm.toUpperCase

  println(s"Kicking the car: ${kick(bmw)}")
  println(s"Kicking the cat: ${kick(garfield)}")
}


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

object Main2 extends App with Data {
  import Implicits._

  def kickObject[T](obj: T)(implicit noiseProducing: NoiseProducing[T]): String =
    noiseProducing.makeNoise(obj).toUpperCase

  def kickObjectAlternative[T : NoiseProducing](obj: T): String =
    implicitly[NoiseProducing[T]].makeNoise(obj).toUpperCase

  println(s"Kicking the car: ${kickObject(bmw)}")
  println(s"Kicking the cat: ${kickObjectAlternative(garfield)}")
}

object Main3 extends App with Data {
  import Implicits._
  import Main2.kickObject

  val noCar: Option[Car] = None
  val myCar: Option[Car] = Some(bmw)

  implicit def optionNoise[T: NoiseProducing] = new NoiseProducing[Option[T]] {
    override def makeNoise(t: Option[T]): String = t match {
      case Some(o) => kickObject(o)
      case None => "......"
    }
  }

  println(s"Kicking the non-existing car: ${kickObject(noCar)}")
  println(s"Kicking some car: ${kickObject(myCar)}")
}


