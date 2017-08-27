// Coursier changes SBT's resolution mechanism to download dependencies in
// parallel.

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC10")

// Scalariform is a code formatter.

addSbtPlugin("com.lucidchart" % "sbt-scalafmt-coursier" % "1.10")

// WartRemover is an aggressive linter.

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.1.1")

// The Supersafe plugin teaches SBT how to check Scalatest assertions for type
// soundness.
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.2")

// Fix partial unification issues.

addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.0")
