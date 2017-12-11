scalaVersion := "2.12.2"

scalacOptions ++= Seq()

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)
libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "org.typelevel" %% "discipline" % "0.8" % "test"
