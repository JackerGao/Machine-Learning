lazy val root = (project in file(".")).
  settings(
    name := "kmeantest",
    version := "1.0",
    scalaVersion := "2.10.5",
    libraryDependencies ++= Seq(
     "org.apache.spark" %% "spark-mllib" % "1.6.0",
      "org.apache.spark" %% "spark-core" % "1.6.0",
      "org.apache.spark" %% "spark-sql" % "1.6.0",
      "org.apache.spark" %% "spark-hive" % "1.6.0",
      "org.apache.spark" %% "spark-hive-thriftserver" % "1.6.0",
      "com.squareup.okhttp" % "okhttp" % "2.7.5",
      "com.alibaba" % "fastjson" % "1.2.12",
      "joda-time" % "joda-time" % "2.7",
      "org.joda" % "joda-convert" % "1.7",
      "com.databricks" % "spark-csv_2.11" % "1.2.0"
    )   
  )
