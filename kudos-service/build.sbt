name := """kudos-service"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

/**
 * Dependencies
 */
libraryDependencies ++= Seq (
  javaWs,
  "com.impetus.kundera.client" % "kundera-cassandra-ds-driver" % "3.13",
  "org.mongodb" % "mongodb-driver" % "3.11.0",
  "com.rabbitmq" % "amqp-client" % "5.7.3",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.9.9",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.9",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.9"
).map(_.force())

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
