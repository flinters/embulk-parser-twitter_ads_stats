lazy val core = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "jp.co.septeni-original",
      scalaVersion := "2.12.3",
      version := "0.1.0-SNAPSHOT"
    )
  ),
  name := "embulk-parser-twitter_ads_stats"
)

enablePlugins(ScalafmtPlugin)

resolvers += Resolver.jcenterRepo
libraryDependencies ++= Dependencies.value
scalacOptions += "-Xexperimental"

scalafmtVersion in ThisBuild := "1.2.0"
scalafmtOnCompile in ThisBuild := true
