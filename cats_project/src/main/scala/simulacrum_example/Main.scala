package simulacrum_example

import Appender.ops._

object Main extends App {
  implicit val appenderInt: Appender[Int] = (x: Int, y: Int) => x + y

  println(1 |+| 2)
}
