name := "server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  "org.apache.uima" % "uimafit-core" % "2.0.0"
)

play.Project.playScalaSettings
