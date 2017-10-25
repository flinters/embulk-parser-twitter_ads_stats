import sbt._

object Dependencies {

  val value = Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % Test,
    "org.embulk" % "embulk-core" % "0.8.35",
    "org.embulk" % "embulk-core" % "0.8.35" classifier "tests",
    "io.spray" %%  "spray-json" % "1.3.3"
  )
}
