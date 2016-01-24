organization  := "com.sprayakkarest"
 
version       := "0.1"
 
scalaVersion  := "2.11.6"

publishMavenStyle := true

crossPaths       := false

autoScalaLibrary := false
 
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

mainClass in Compile := Some("com/sprayakkarest.akka.main.ruleprocessor.MainRuleProcessor")
 
libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV withSources() withJavadoc(),
    "io.spray"            %%  "spray-routing" % sprayV withSources() withJavadoc(),
    "io.spray"            %%  "spray-json"    % "1.3.0",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "org.scalaz"          %%  "scalaz-core"   % "7.1.0"
  )
}