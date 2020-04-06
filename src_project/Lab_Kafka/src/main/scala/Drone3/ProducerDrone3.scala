package Drone3

import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Calendar, Date, Properties}

import Utils.Constants
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object ProducerDrone1 extends App {

  class Message(var id: Int, var time: String, var coords: (Int, Int))

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  //drone ID
  val id = 3

  // new producer
  val producer = new KafkaProducer[String, String](props)


  // Periodic notifications simulation
  while(true) {
    // Random Generator
    val rnd = new scala.util.Random

    // Geographic coordinates
    val x = Constants.startC + rnd.nextInt( (Constants.endC - Constants.startC) + 1 )
    val y = Constants.startC + rnd.nextInt( (Constants.endC - Constants.startC) + 1 )

    var caseD: Int = 0
    val m = rnd.nextInt(100)
    m match {
      case m if m<=75 => caseD = 1 // Checkpoint
      case m if m>75 && m<=90 => caseD = 2 // Alert
      case m if m>90 => caseD = 3 // Intervention
    }

    //TODO
    // PREPARE DATA
    // Date
    val date: Date = Calendar.getInstance().getTime()
    val dateFormat: DateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
    val strDate: String = dateFormat.format(date)

    //TODO
    caseD match {
      // PERIODIC NOTIFICATION
      case 1 => producer.send(new ProducerRecord[String, String]("PERIODIC",
        "------CHECKPOINT------"
          + "\nID Drone : " + id.toString
          + "\nDate : " + strDate
          + "\nCoordinates : ("+ x.toString +";"+ y.toString + ")"))
      // ALERT
      case 2 => {
        val natureAlert = Constants.possibleAlerts(
          rnd.nextInt(Constants.possibleAlerts.length)
        )
        producer.send(new ProducerRecord[String, String]("ALERT",
          "------ALERT------"
            +"\nID Drone : " + id.toString
            + "\nDate : " + strDate
            +"\nCoordinates : ("+ x.toString +";"+ y.toString + ")"
            +"\nAlert : "+ natureAlert))
      }
      // HUMAN INTERVENTION
      case _ => producer.send(new ProducerRecord[String, String]("SOS-SENDER", id.toString + "," + strDate +","+ x.toString +","+ y.toString
        + "," + Constants.picture))
    }
    Thread.sleep(500)
  }
}
