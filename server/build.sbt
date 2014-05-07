name := "server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.apache.uima" % "uimafit-core" % "2.0.0"
)

play.Project.playScalaSettings
