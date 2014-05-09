name := "server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.apache.uima" % "uimafit-core" % "2.0.0",
  "org.webjars" % "bootstrap" % "3.1.1-1"
)

play.Project.playScalaSettings
