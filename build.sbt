import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := ScalaVersions.scala3

lazy val root = (project in file("."))
  .settings(
    name := "SubscriptionSystem",
    idePackagePrefix := Some("Alkimi"),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % Versions.zio,
      "dev.zio" %% "zio-json" % Versions.zioJson,
      "io.d11" %% "zhttp" % Versions.zhttp,
      "dev.zio" %% "zio-test" % Versions.zio % "test",
      "dev.zio" %% "zio-test-sbt" % Versions.zio % "test",
      "dev.zio" %% "zio-test-junit" % Versions.zio % "test"
    )
  )

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
