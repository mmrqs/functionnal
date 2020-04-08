package TriForce

import java.util
import java.util.Properties

import Utils.Constants
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.collection.JavaConverters._

class Dispatcher (var pacerelles : Map[Int, Bridge]) extends Thread {

  override def run() {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "latest")
    props.put("group.id", "consumer-group")
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Arrays.asList("SOS-RESPONSE"))

    val prope = new Properties()
    prope.put("bootstrap.servers", "localhost:9092")
    prope.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    prope.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    // new producer
    val producer = new KafkaProducer[String, String](prope)

    while (true) {
      val record = consumer.poll(1000).asScala
      for (data <- record.iterator) {
        val string = data.value()
        val myData = string.split(",")
        val idDrone = myData(0)
        val code = myData(1)

        println("-----------------------------------------------------")
        println("Received ERROR CODE: " + string)

        if (Constants.possibleAlerts.get(code)== None) {
          producer.send(new ProducerRecord[String, String]("SOS-SENDER", idDrone
            +"\n" + Constants.picture
            +"\n ERROR WITH YOUR PRECEDENT SOLUTION : CODE" +code+"DOESN'T EXIST"))
        } else if( pacerelles.get(idDrone.toInt).get == None ){
          producer.send(new ProducerRecord[String, String]("SOS-SENDER",
            "Picture : " + Constants.picture
            +"\n ERROR WITH YOUR PRECEDENT SOLUTION : DRONE ID " +idDrone+" DOESN'T EXIST"))
        } else if (!pacerelles.get(idDrone.toInt).get.requestHelp) {
          producer.send(new ProducerRecord[String, String]("SOS-SENDER",
              "\nERROR WITH YOUR PRECEDENT SOLUTION : DRONE ID " +idDrone+" DOESN'T NEED HELP"))
        } else {
          pacerelles.get(idDrone.toInt).get.produce(code)
        }
      }
      Thread.sleep(1000)
    }
  }
}