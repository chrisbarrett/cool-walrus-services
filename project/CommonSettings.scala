package cool.walrus

import sbt.Keys._
import sbt._

import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import scoverage.ScoverageKeys._
import wartremover._

object CommonSettings {
  lazy val defaults =
    basicSettings ++ dirSettings ++ promptSettings ++ coverageSettings ++ wartSettings ++ formatterSettings

  // Define general settings.

  lazy val basicSettings = Seq(
    organization  := "mm",
    scalaVersion  := "2.12.2",
    scalacOptions := Seq("-Xlint", "-deprecation", "-feature", "-language:higherKinds", "-language:implicitConversions"),
    fork in Test := true,
    parallelExecution in Test := false,

    addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M9" cross CrossVersion.full),

    resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )

  // Add a winsome walrus emoticon to the prompt.

  lazy val promptSettings = Seq(
    shellPrompt := { s: State =>
      val c = scala.Console
      val blue = c.RESET + c.BLUE + c.BOLD
      val white = c.RESET + c.BOLD
      val walrus = s"${white}(:3 っ)${c.RESET}"
      val project = s"${blue}${Project.extract(s).currentProject.id}${c.RESET}"
      s" $walrus [$project] "
    }
  )

  // Override the directory structure settings so that subprojects have the
  // following flattened layout:
  //
  // build.sbt
  // resources/
  //   application.conf
  // src/
  //   A.scala
  // test/
  //   ATests.scala

  lazy val dirSettings = Seq(
    sourceDirectory in Compile := baseDirectory.value / "src",
    sourceDirectory in Test := baseDirectory.value / "test",

    scalaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test",

    resourceDirectory in Compile := baseDirectory.value / "resources",
    resourceDirectory in Test := baseDirectory.value / "resources_test",

    unmanagedSourceDirectories in Compile += baseDirectory.value / "src_generated",
    unmanagedResourceDirectories in Compile += baseDirectory.value / "resources_generated"
  )

  // Add a `cover` command to run tests and generate coverage reports.

  val cover = taskKey[Unit]("Run coverage")

  lazy val coverageSettings = Seq(
    coverageMinimum := 90,
    coverageFailOnMinimum := true,
    coverageExcludedPackages += "<empty>;^Main;cool.walrus.Main",
    coverageEnabled := true,

    cover := {
      (coverageReport in Test) dependsOn
      (test in Test) dependsOn
      (clean in Test)
    }.value
  )

  // Add wartremover as a compiler plugin so that warts are reported as compiler
  // errors.

  lazy val wartSettings = Seq(
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.wartremover" %% "wartremover" % "2.1.1"),
    scalacOptions += "-P:wartremover:traverser:org.wartremover.warts.Unsafe"
  )

  // Scalafmt configuration.

  lazy val formatterSettings = Seq(
    scalafmtOnCompile := true
  )
}
