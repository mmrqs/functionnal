package Technician

import java.util.{Calendar, Date, Properties}
import scala.io.StdIn.readLine
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object ProducerTechnician extends App {

  class Message(var id: Int, var time: String, var coords: (Int, Int))

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")


  // new producer
  val producer = new KafkaProducer[String, String](props)

  // Periodic notifications simulation
  while(true) {

    print("Drone id :")
    val droneID = readLine()
    print("CODE : ")
    val CODE = readLine()
    producer.send(new ProducerRecord[String, String]("SOS-RESPONSE-" + droneID.toString,
      CODE.toString))
    println("Response sent")
  }
}
