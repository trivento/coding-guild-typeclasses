package simulacrum_example

import Appender.ops._

object Main extends App {
  implicit val appenderInt: Appender[Int] = new Appender[Int] {
    def append(x: Int, y: Int) = x + y
  }

  println(1 |+| 2)
}
