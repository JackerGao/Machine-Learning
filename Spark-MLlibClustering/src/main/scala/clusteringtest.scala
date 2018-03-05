package com.kmeans.testing

import org.apache.spark.sql.SQLContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.sql.functions._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.DataFrame
import org.joda.time.DateTime
import org.apache.spark.sql.SaveMode
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.mllib.clustering.{GaussianMixture, GaussianMixtureModel}
import org.apache.spark.mllib.clustering.BisectingKMeans
object kmeanstest{

//case class CC1(ID: String, LABEL: String, RTN5: Double, FIVE_DAY_GL: Double, CLOSE: Double, RSI2: Double, RSI_CLOSE_3: Double, PERCENT_RANK_100: Double, RSI_STREAK_2: Double, CRSI: Double)

def main(args:Array[String]):Unit = {
val conf = new SparkConf().setAppName("kmeanstest")
val sc = new SparkContext(conf)
val hiveContext = new HiveContext(sc)
import hiveContext.implicits._

// load file and remove header
val raw_data_df = hiveContext.sql("select * from (select * from mart_operation_platform.anti_feature_all_partitions where dt='2018-02-10') X inner join (select distinct distinct_id from mart_operation_platform.public_sensor_actuser where dt='2018-02-10' and type in (1,2)) Y on X.jdbid=Y.distinct_id")
val raw_data = raw_data_df.drop("ip_province").drop("dt").drop("distinct_id")
val col_length = raw_data.columns.length
val allData = raw_data.map{s =>
    val jdbid = s(0).toString
    val fetureData = (1 to col_length-1).map(x=>s(x).toString.toDouble)
	val featureVector = Vectors.dense(fetureData.toArray)
    (jdbid,featureVector)
}
allData.cache()
//KMeans model with 2 clusters and 200 iterations
val kMeansModel = KMeans.train(allData.map(x=>x._2), 10, 200)
//Print the center of each cluster
kMeansModel.clusterCenters.foreach(println)
// Get the prediction from the model with the ID so we can link them back to other information
val predictions = allData.map(r => (r._1, kMeansModel.predict(r._2)))
// convert the rdd to a dataframe
val predDF = predictions.toDF("jdbid", "kmeanscluster")
//GMM
val gmm = new GaussianMixture().setK(10).setMaxIterations(200).run(allData.map(x=>x._2))
val gmmcluster = allData.map(x=>(x._1,gmm.predict(x._2))).toDF("jdbid","gmmcluster")
//PIC-power iteration clustering---graph clustering
//bisecting K-means----hierarchical clustering
val bkm = new BisectingKMeans().setK(10).setMaxIterations(200)
val bkmodel = bkm.run(allData.map(x=>x._2))
val bkcluster = allData.map(x=>(x._1,bkmodel.predict(x._2))).toDF("jdbid","bkmcluster")
val clusterResult = predDF.join(gmmcluster,Seq("jdbid"),"inner").join(bkcluster,Seq("jdbid"),"inner")
clusterResult.show()
clusterResult.registerTempTable("result")
hiveContext.sql("DROP TABLE IF EXISTS mart_operation_platform.anti_fraud_kmeans_result")
hiveContext.sql("create table mart_operation_platform.anti_fraud_kmeans_result as select * from result")
val outliers_score = hiveContext.sql("select * from mart_operation_platform.wuzj_iforest_test order by anomalyscore desc")
val resultData = clusterResult.join(outliers_score,Seq("jdbid"),"inner").sort($"anomalyscore".desc)
hiveContext.sql("DROP TABLE IF EXISTS mart_operation_platform.anti_fraud_kmeans_result_outlier")
resultData.registerTempTable("t")
hiveContext.sql("create table mart_operation_platform.anti_fraud_kmeans_result_outlier as select * from t")
val kcluster_anal = hiveContext.sql("select kmeanscluster,score_sum,cnt,score_sum/cnt score_median from (select kmeanscluster,sum(anomalyscore) score_sum,count(1) cnt from mart_operation_platform.anti_fraud_kmeans_result_outlier group by kmeanscluster)A")
val gmmcluster_anal = hiveContext.sql("select gmmcluster,score_sum,cnt,score_sum/cnt score_median from (select gmmcluster,sum(anomalyscore) score_sum,count(1) cnt from mart_operation_platform.anti_fraud_kmeans_result_outlier group by gmmcluster)A")
val bkmcluster_anal = hiveContext.sql("select bkmcluster,score_sum,cnt,score_sum/cnt score_median from (select bkmcluster,sum(anomalyscore) score_sum,count(1) cnt from mart_operation_platform.anti_fraud_kmeans_result_outlier group by bkmcluster)A")
kcluster_anal.show
gmmcluster_anal.show
bkmcluster_anal.show
//resultData.write
//    .format("com.databricks.spark.csv")
//    .option("header", "true")
//    .save("hdfs://reh/user/hive/bdm/mart_mperation_platform/gaosl/kmeans++/kmeans_result.csv")
//resultData.select("jdbid","anomalyscore","cluster").write.save("hdfs://reh/user/hive/bdm/mart_mperation_platform/gaosl/kmeans++/kmeans_result.csv")
//resultData.write.format("com.databricks.spark.csv").save("hdfs://reh/user/hive/bdm/mart_mperation_platform/gaosl/kmeans++/kmeans_result.csv")

}
}
