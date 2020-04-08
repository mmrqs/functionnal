package Suparuku
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.log4j.Logger

import org.apache.log4j.Level

object SparkPOC extends App {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  val conf = new SparkConf().setAppName("FDP").setMaster("local[*]")
  val streamingContext = new StreamingContext(conf, Seconds(1))

  val kafkaParams = Map[String, Object] (
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "i_must_be_unique_for_each_stream",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" -> (false: java.lang.Boolean),
  )

  val topics = Array("ALERT")
  val stream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  stream.foreachRDD(rdd => rdd.map(record => record.value.split(',')).foreach(s => println("The key is "
    + s.apply(0)
    + " and the value is "
    + s.apply(1))))
  streamingContext.start()
  streamingContext.awaitTermination()
}
