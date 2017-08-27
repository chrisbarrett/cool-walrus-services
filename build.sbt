import cool.walrus._

lazy val root = project
  .walrus(".")
  .aggregate(`org-capture`)

val FreesVersion = "0.3.1"

lazy val `org-capture` = project
  .walrus("org-capture")
  .settings(
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig" % "0.7.2",
      "com.softwaremill.quicklens" %% "quicklens" % "1.4.8",
      "io.frees" %% "freestyle" % FreesVersion,
      "io.frees" %% "freestyle-doobie" % FreesVersion,
      "io.frees" %% "freestyle-http-finch" % FreesVersion,
      "io.frees" %% "freestyle-logging" % FreesVersion,
      "org.typelevel" %% "kittens" % "1.0.0-M10",
      "org.tpolecat" %% "doobie-h2-cats" % "0.4.4" % Test
    )
  )
