package Drone2

import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Calendar, Date, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object ProducerDrone2 extends App {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  // bornes random id
  val start = 1
  val end   = 10

  // bornes coordinates
  val startC = 1
  val endC = 100

  //bornes cases :
  val startP = 1
  val endP = 3

  //drone ID
  val id = 2

  //Picture :
  val picture = "http://vaniquertonpanda.com"

  // Alert message possibilities
  val possibleAlerts = Seq(
    "666 - Pandora a mangé le drone",
    "001 - Ben fait du Biryani sur la voie publique",
    "002 - Anil tente d'assassiner Brenda",
    "003 - Louis a laissé ses bouteilles de vodka dans la rue",
    "004 - Mélanie a mit un air KO à CHEREL",
    "005 - Sébastien deal des pizzas",
    "006 - Kévin lance une révolution",
    "007 - Thomas bricole le drone"
  )

  // new producer
  val producer = new KafkaProducer[String, String](props)



  // Periodic notifications simulation
  while(true) {

    val rnd = new scala.util.Random
    val caseD = startP +  rnd.nextInt( (endP - startP) + 1 )

    //TODO
    // PREPARE DATA

    // Date
    val date: Date = Calendar.getInstance().getTime()
    val dateFormat: DateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
    val strDate: String = dateFormat.format(date)

    // Geographic coordinates
    val x = startC + rnd.nextInt( (endC - startC) + 1 )
    val y = startC + rnd.nextInt( (endC - startC) + 1 )


    //TODO
    // PERIODIC NOTIFICATIONS :
    if( caseD == 1) {
      producer.send(new ProducerRecord[String, String]("PERIODIC",
        "------CHECKPOINT------"
          + "\nID Drone : " + id.toString
          + "\nDate : " + strDate
          + "\nCoordinates : ("+ x.toString +";"+ y.toString + ")"))
    }

    //TODO
    // ALERTS :
    //we generate a random alert
    else if (caseD == 2) {
      val natureAlert = possibleAlerts(
        rnd.nextInt(possibleAlerts.length)
      )
      producer.send(new ProducerRecord[String, String]("ALERT",
        "------ALERT------"
          +"\nID Drone : " + id.toString
          + "\nDate : " + strDate
          +"\nCoordinates : ("+ x.toString +";"+ y.toString + ")"
          +"\nAlert : "+ natureAlert))
    }


    //TODO
    // SEND HELP :
    else {
      producer.send(new ProducerRecord[String, String]("SOS-SENDER", id.toString + "," + strDate +","+ x.toString +","+ y.toString
        + "," + picture))
    }

    Thread.sleep(50000)
  }
}
