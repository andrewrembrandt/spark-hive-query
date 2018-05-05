package uk.me.rembrandt.sparkhivespike

import org.apache.spark.sql.SparkSession

object SecurityApp {

  def main(args: Array[String]): Unit = {
    val session = SparkSession
      .builder()
      .master("local")
      .appName("SecurityApp")
      .config("spark.sql.wharehouse.dir", "/user/hive/warehouse")
      .enableHiveSupport()
      .getOrCreate()

    SecurityEtl.process(session)

    session.stop()
  }
}


object SecurityEtl {
  def process(session: SparkSession) = {
    val sc = session.sparkContext

    import session.implicits._

    val secDf = session.table("security_raw")
    val secDetails = secDf.as[SecurityDetails]
    secDetails.show()
  }
}