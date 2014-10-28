name := "readability-server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  "org.apache.uima" % "uimafit-core" % "2.0.0",
  "eu.crydee.readability" % "corpus" % "1.0.0-SNAPSHOT",
  "eu.crydee.readability" % "uima-server" % "1.0.0-SNAPSHOT" classifier "fatjar"
)

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

play.Project.playScalaSettings
