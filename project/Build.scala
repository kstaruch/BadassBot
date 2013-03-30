import sbt._
import Keys._

object Build extends Build {
  val botDirectory = SettingKey[File]("bot-directory")
  val play = TaskKey[Unit]("play") <<= (botDirectory, name, javaOptions,
    unmanagedClasspath in Compile,
    Keys.`package` in Compile) map {
    (bots, name, javaOptions, ucp, botJar) =>

      IO createDirectory (bots / name)
      IO copyFile (botJar, bots / name / "ScalatronBot.jar")

     /* val cmd = "java %s -cp %s scalatron.main.Main -plugins %s" format (
        javaOptions mkString " ",
        Seq(ucp.files.head, botJar).absString,
        bots.absolutePath)*/
      //-x 150 -y 150
     val cmd = "java %s -cp %s -jar D:/dev/Scalatron/bin/Scalatron.jar -plugins %s -browser no" format (
       javaOptions mkString " ",
       Seq(ucp.files.head, botJar).absString,
       bots.absolutePath)
      cmd run
  }

  val bot = Project(
    id = "BadassBot",
    base = file("."),
    settings = Project.defaultSettings ++ botSettings)

  val botSettings = Seq[Setting[_]](
    organization := "kstaruch",
    name := "BadassBot",
    version := "1.0-SNAPSHOT",

    scalaVersion := "2.9.1",
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    javaOptions += "-Xmx1g -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005",

    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.8.1" % "test",
      "org.scalatest" %% "scalatest" % "1.8" % "test",
      "net.debasishg" %% "sjson" % "0.17" % "compile"
    ),

    botDirectory := file("bots"),
    play)
}