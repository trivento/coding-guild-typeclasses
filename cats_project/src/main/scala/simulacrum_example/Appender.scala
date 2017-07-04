package simulacrum_example

import simulacrum._

@typeclass trait Appender[A] {
  @op("|+|") def append(x: A, y: A): A
}