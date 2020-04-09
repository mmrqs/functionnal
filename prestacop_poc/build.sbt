name := "prestacop_poc"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.4.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.2.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10-assembly" % "2.2.0"

libraryDependencies += "org.vegas-viz" %% "vegas" % "0.3.11"
