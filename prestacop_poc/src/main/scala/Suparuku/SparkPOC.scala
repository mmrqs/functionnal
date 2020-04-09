package Suparuku
import Model.Ticket
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._

import scala.collection.mutable

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

  val hashmapVC = new mutable.HashMap[Int, Int]()

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



  // Dates with number of violation

  stream.foreachRDD(rdd => rdd.map(record => new Ticket(record.value().split(',')))
    .map(ticket => (ticket.getDate, 1))
    .reduceByKey(_ + _)
    .sortByKey(false, 1)
    .takeOrdered(10)(Ordering[Int].reverse.on(t => t._2))
    .foreach(println))


  streamingContext.start()
  streamingContext.awaitTerminationOrTimeout(60000)
  print(hashmapVC.toSeq.sortBy(_._2))
}
