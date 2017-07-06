package generic

trait NamePrintingApp extends App {
  println(s"---- ${this.getClass.getSimpleName.replace('$', ' ')} ----")
}
