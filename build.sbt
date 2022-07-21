val http4sVersion = "0.21.22"
val specs2Version = "4.11.0"
val log4CatsVersion = "1.3.0"
val slf4jVersion = "1.7.30"
val kindProjectorVersion = "0.11.3"
val circeVersion = "0.12.3"
val doobieVersion = "0.9.0"
val pureconfigVersion = "0.17.1"

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.13.5",
    organization := "ru.dm4x",
    name := "dummy_tldr_bot",
    version := "1.0.0-SNAPSHOT",
    scalacOptions ++= Seq(
      "-encoding", "utf8",
      "-Xfatal-warnings",
      "-deprecation",
      "-unchecked",
      "-deprecation",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:postfixOps",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates",
    ),
    libraryDependencies ++= Seq(
      compilerPlugin(("org.typelevel" %% "kind-projector" % kindProjectorVersion).cross(CrossVersion.full)),
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "org.typelevel" %% "log4cats-core" % log4CatsVersion,
      "org.typelevel" %% "log4cats-slf4j" % log4CatsVersion,
      "org.slf4j" % "slf4j-simple" % slf4jVersion,
      "org.specs2" %% "specs2-core" % specs2Version % "test",
      "com.github.pureconfig" %% "pureconfig" % pureconfigVersion
    )
  )

lazy val app = (project in file("app"))
  .settings(
    assembly / assemblyJarName := "dummyTldrBot.jar",
    assembly / mainClass := Some("ru.dm4x.dummy_tldr_bot.botLogic.App"),
  )

