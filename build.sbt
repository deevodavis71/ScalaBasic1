enablePlugins(AssemblyPlugin)

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

ThisBuild / fork := true

lazy val root = (project in file("."))
  .settings(
    name := "ScalaBasic1"
  )

val Http4sVersion = "0.23.12"
val CatsEffectVersion = "3.5.3"
val CirceVersion = "0.14.6"
val DoobieVersion = "1.0.0-RC4"
val PureConfigVersion = "0.17.9"
val ScalaTestVersion = "3.2.19"

// Cats Effects for Async code
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % CatsEffectVersion
)

// Circe for JSON serialisation
libraryDependencies ++= Seq(
  "io.circe" %% "circe-generic" % CirceVersion,
)

// HTTP4S for API Routes
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion
)

// Doobie for DB work
libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-h2" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
)

// Application properties
libraryDependencies ++= Seq(
  "com.github.pureconfig" %% "pureconfig-core" % PureConfigVersion
)

// Test dependencies
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
)

Test / scalacOptions += "-Wconf:cat=other-pure-statement&msg=org.scalatest.Assertion:s"

// Assembly support
assembly / mainClass := Some("MainApp")

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
