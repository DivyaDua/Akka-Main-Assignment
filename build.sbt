name := "AkkaMajorAssignment"

version := "1.0"

scalaVersion := "2.12.1"

val akkaVersion = "2.4.17"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1"
)

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"

libraryDependencies += "log4j" % "log4j" % "1.2.17"

//libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.3" % "test"
        