package Drone3

import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

import scala.collection.JavaConverters._

object ConsumerDrone3 extends App {
  class Message(var id: Int, var time: String, var coords: (Int, Int))
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("auto.offset.reset", "earliest")
  props.put("group.id", "consumer-group")
  val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
  consumer.subscribe(util.Arrays.asList("patrol"))
  while (true) {
    val record = consumer.poll(1000).asScala
    for (data <- record.iterator) {
      val string = data.value()
      val myData = string.split(",")

      println("-----------------------------------------------------")
      println("Drone id: " + myData(0) + "\nTime: " + myData(1)
        + "\nCoords: (x=" + myData(2) + ", y=" + myData(3) + ")")
    }
  }
}