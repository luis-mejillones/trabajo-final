name := """stats-service"""

version := "1.0-SNAPSHOT"

lazy val root = Project( id = "stats-service", base = file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

/**
 * Dependencies
 */
libraryDependencies ++= Seq (
  javaWs,
  guice,
  "org.mongodb" % "mongodb-driver" % "3.11.0",
  "com.rabbitmq" % "amqp-client" % "5.7.3",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.9.9",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.9",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.9",
  "org.apache.httpcomponents" % "httpclient" % "4.5.10"
).map(_.force())

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
