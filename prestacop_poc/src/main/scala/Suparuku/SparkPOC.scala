package Suparuku
import Model.Ticket
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import vegas.DSL.Vegas
import vegas._

import scala.collection.mutable

/**
 * Read all the alerts and display statistics
 */

object SparkPOC extends App {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  val conf = new SparkConf().setAppName("FDP").setMaster("local[*]")
  val streamingContext = new StreamingContext(conf, Seconds(10))

  val kafkaParams = Map[String, Object] (
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "i_must_be_unique_for_each_stream",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val topics = Array("ALERT")
  val stream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  // HashMap with the number of infractions according to a specific violation code
  val hashmapVC = new mutable.HashMap[Int, Int]()

  // HashMap with the number of infractions according to a specific date
  val hashmapDate = new mutable.HashMap[String, Int]()

  stream.foreachRDD(rdd => rdd.map(record => new Ticket(record.value().split(',')))
    .map(ticket => (ticket.getVC, 1))
    .reduceByKey(_ + _)
    .foreach(tuple => {
      if (hashmapVC.contains(tuple._1)) {
        hashmapVC(tuple._1) += tuple._2
      } else {
        hashmapVC += tuple
      }
    })
  )

  stream.foreachRDD(rdd => rdd.map(record => new Ticket(record.value().split(',')))
    .map(ticket => (ticket.getDate, 1))
    .reduceByKey(_ + _)
    .foreach(tuple => {
      if (hashmapDate.contains(tuple._1)) {
        hashmapDate(tuple._1) += tuple._2
      } else {
        hashmapDate += tuple
      }
    })
  )

  streamingContext.start()
  streamingContext.awaitTerminationOrTimeout(60000)

  //GRAPHS :

  // Number of Infractions per violation code :
  var plot = Vegas("Number of Infractions per violation code : ").
    withData(
      hashmapVC.toSeq.sortBy(_._2).map(x => Map("Violation code" -> x._1, "Number of violations" -> x._2))
    ).encodeY("Number of violations", Quant)
    .encodeX("Violation code", Nom).
    mark(Bar)
  plot.show

  // 10 less perpetrated violation code :
  var plot2 = Vegas("Top 10 less perpetrated violation code : ").
    withData(
      hashmapVC.toSeq.sortBy(_._2).take(10).map(x => Map("Violation code" -> x._1, "Number of violations" -> x._2))
    ).encodeY("Number of violations", Quant)
    .encodeX("Violation code", Nom).
    mark(Bar)
  plot2.show

  // 10 most perpetrated violation code
  var plot3 = Vegas("Top 10 most perpetrated violation code : ").
    withData(
      hashmapVC.toSeq.sortBy(_._2).reverse.take(10).map(x => Map("Violation code" -> x._1, "Number of violations" -> x._2))
    ).encodeY("Number of violations", Quant)
    .encodeX("Violation code", Nom).
    mark(Bar)
  plot3.show

  // Graph TOP 20 Dates with number of violation
  var plot4 = Vegas("Top 20 dates with most violations : ").
    withData(
      hashmapDate.toSeq.sortBy(_._2).reverse.take(10).map(x => Map("Date" -> x._1, "Number of violations" -> x._2))
    ).encodeY("Number of violations", Quant)
    .encodeX("Date", Nom).
    mark(Bar)
  plot4.show
}
