package Model

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object TicketsPOC extends App {
  val pathToFile = "/home/pridethedaemon/Documents/scala/functionnal/data/2015.csv"

  def parseFromCsv(pathToFile: String):List[Ticket] = {
    val tickets = ArrayBuffer[Ticket]()
    val bufferedSource = Source.fromFile(pathToFile)
    for (line <- bufferedSource.getLines.drop(1)) {
      producer.send(new ProducerRecord[String, String]("ALERT", line))
    }
    bufferedSource.close
    tickets.toList
  }

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  // new producer
  val producer = new KafkaProducer[String, String](props)

  val ticketList = parseFromCsv(pathToFile)

  producer.close()
}