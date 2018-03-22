name := "slick-example"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.2",

  "com.h2database" % "h2" % "1.4.197"
)