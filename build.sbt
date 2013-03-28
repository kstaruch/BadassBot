name := "BadassBot"

artifactName := { (_, _, _) => "ScalatronBot.jar" }

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
      "junit" % "junit" % "4.8.1" % "test",
      "org.scalatest" %% "scalatest" % "1.8" % "test"
    )
