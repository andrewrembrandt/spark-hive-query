package uk.me.rembrandt.sparkhivespike

import java.sql.{Date, Timestamp}
import java.time.format.DateTimeFormatter
import java.time.LocalDate

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

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

    import session.implicits._

    val isNewCreator = udf((insDateStr: String) => {
      val insDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
      val insDate = Date.valueOf(
        LocalDate.parse(insDateStr.trim.split(' ').head, insDateFormat)
      )
      val newBoundary = Date.valueOf(LocalDate.now.minusMonths(12))
      insDate.after(newBoundary)
    }).apply($"InsDt")

    val secDf = session.table("security_raw")
      .drop("PrefixId")
      .drop("InsSrc")
      .drop("UpdSrc")
      .withColumn("NormalisationDate", current_timestamp())
      .withColumn("IsNew", isNewCreator)

    val secDetails = secDf.as[SecurityDetails]

    secDetails.show()
  }
}