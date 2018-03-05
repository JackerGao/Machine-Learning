#! /bin/bash
#cal borrow amount last 1-15days
#cal user profile tag

#get current date into push statistics result file

#spark-submit --class com.dtspark.scala.basics.credit_score --driver-memory 50G --executor-memory 25G --executor-cores 2 --num-executors 200 --master yarn-client --queue root.bigdata.friend /home/mop_strategy_group/zhangfg/psl_info/user_credit_score/credit_score/credit_score/target/scala-2.10/credit_score_2.10-1.0.jar

#/home/mop_strategy_group/huangxy/datagent/script/hive2es/public_credit_score.sh

spark-submit --class com.kmeans.testing.kmeanstest  --driver-memory 20G --executor-memory 25G --executor-cores 2 --num-executors 50 --master yarn-client --queue root.bigdata.friend /home/mop_strategy_group/Shuli_Gao/model/anti_fraud/antifraudKmeans++/target/scala-2.10/kmeantest_2.10-1.0.jar 
