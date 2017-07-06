scalaVersion := "2.12.2"

libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"

scalacOptions ++= Seq()

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

val paradiseVersion = "2.1.0"

addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)
libraryDependencies ++= (
  if (scalaVersion.value.startsWith("2.10"))
    List("org.scalamacros" %% "quasiquotes" % paradiseVersion)
  else
    Nil
)
