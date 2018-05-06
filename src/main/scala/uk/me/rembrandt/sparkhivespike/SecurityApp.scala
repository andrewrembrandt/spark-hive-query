package uk.me.rembrandt.sparkhivespike

import java.sql.{Date, Timestamp}
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import java.util.Calendar

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object SecurityApp {

  def main(args: Array[String]): Unit = {
    val session = SparkSession
      .builder()
      .master("local")
      .appName("SecurityApp")
      .config("spark.sql.wharehouse.dir", "/user/hive/warehouse")
      .config("spark.sql.hive.metastore.version", "2.1")
      .config("spark.sql.hive.metastore.jars", sys.env.get("HIVE_HOME").map(hh => s"$hh/lib/*").getOrElse("maven"))
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

    val prefixProdIssYearInterim = secDetails.groupByKey(sd => (sd.prefix, sd.product, sd.issDt.take(4).toInt, sd.issAmt))
      .agg(min("IssAmt").as[Double], max("IssAmt").as[Double], sum("IssAmt").as[Double])
    prefixProdIssYearInterim.show()

    val prefixProdIssYear = prefixProdIssYearInterim.select("key._1", "key._2", "key._3", "min(IssAmt)", "max(IssAmt)", "sum(IssAmt)")
      .withColumnRenamed("_1", "Prefix").withColumnRenamed("_2", "Product").withColumnRenamed("_3", "IssueYear")
    prefixProdIssYear.show()

    val prefixProductTotIssueAmounts = prefixProdIssYear.groupBy("Product").pivot("Prefix").sum("sum(IssAmt)").na.fill(0.0)
    prefixProductTotIssueAmounts.show()

    secDetails.show()
  }
}