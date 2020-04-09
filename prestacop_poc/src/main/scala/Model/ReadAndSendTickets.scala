package Model

import java.text.SimpleDateFormat
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source

/**
 * Reads the csv, converts each line into a ticket and sends it to kafka
 */

object ReadAndSendTickets extends App {
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val pathToFile = "/Users/melaniemarques/Downloads/nyc-parking-tickets/Parking_Violations_Issued_-_Fiscal_Year_2015.csv"

  val bufferedSource = Source.fromFile(pathToFile)
  val simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy")

  val producer = new KafkaProducer[String, String](props)

  bufferedSource.getLines.drop(1).foreach(line => producer
    .send(new ProducerRecord[String, String]("ALERT", new Ticket(0, line.split(','), simpleDateFormat).toString)))

  producer.close()
  bufferedSource.close
}