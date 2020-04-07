package TriForce

import java.io.{ByteArrayOutputStream, File}
import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Base64, Calendar, Date, Properties}

import Utils.Constants
import javax.imageio.ImageIO
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class Producer(var id : Int, var pacerelle: Bridge) extends Thread {
  var topicAlert = "ALERT"
  var topicPeriodicCheckpoint = "PERIODIC"
  var topicSendHelp = "SOS-SENDER"
  var topicImages = "IMAGES"

  override def run() {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

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
        case m if m <= 75 => caseD = 1 // Checkpoint
        case m if m > 75 && m <= 90 => caseD = 2 // Alert
        case m if m > 90 => caseD = 3 // Intervention
      }
      
      caseD = 2

     // PREPARE DATA
      // Date
      val date: Date = Calendar.getInstance().getTime()
      val dateFormat: DateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
      val strDate: String = dateFormat.format(date)

      caseD match {
        // PERIODIC NOTIFICATION
        case 1 => producer.send(new ProducerRecord[String, String]("PERIODIC",
          "------CHECKPOINT------"
            + "\nID Drone : " + id.toString
            + "\nDate : " + strDate
            + "\nCoordinates : ("+ x.toString +";"+ y.toString + ")"))
        // ALERT
        case 2 => {
          val natureAlert = Constants.possibleAlerts.toSeq(
            rnd.nextInt(Constants.possibleAlerts.size)
          )
          producer.send(new ProducerRecord[String, String](topicAlert,
            "------ALERT------"
              + "\nID DRONE : " + id.toString
              + "\nDate : " + strDate
              + "\nCoordinates : ("+ x.toString +";"+ y.toString + ")"
              + "\nAlert : "+ natureAlert
              + "\nImage key : " + id.toString + "-" + strDate + "-" + natureAlert._1))
          var baos = new ByteArrayOutputStream();
          ImageIO.write(ImageIO.read(new File(Constants.possibleImages(rnd.nextInt(Constants.possibleImages.size)))),
            "jpg", baos);
          producer.send(new ProducerRecord[String, String](topicImages, id.toString + "-" + strDate + "-" + natureAlert._1,
            Base64.getEncoder().encodeToString(baos.toByteArray())))

        }
        // HUMAN INTERVENTION
        case _ => producer.send(new ProducerRecord[String, String](topicSendHelp,
          "\nID DRONE : " +id.toString
            + "\nDATE : " + strDate
            + "\nCOORDINATES : ("+ x.toString +";"+ y.toString + ")"
            + "\nPICTURE OF THE INFRACTION : " + Constants.picture))
          var codeP = pacerelle.consume

          producer.send(new ProducerRecord[String, String](topicAlert,
            "------ALERT------"
              + "\nID DRONE : " + id.toString
              + "\nDATE : " + strDate
              + "\nCOORDINATES : ("+ x.toString +";"+ y.toString + ")"
              + "\nALERT CODE : "+ codeP
              + "\nALERT DESCRIPTION : " + Constants.possibleAlerts.get(codeP).toString
              + "\nImage Key :" + id.toString + "-" + strDate + "-" + codeP))

          var baos = new ByteArrayOutputStream();
          ImageIO.write(ImageIO.read(new File(Constants.possibleImages(rnd.nextInt(Constants.possibleImages.size)))),
            "jpg", baos);
          producer.send(new ProducerRecord[String, String](topicImages, id.toString + "-" + strDate + "-" + codeP,
            Base64.getEncoder().encodeToString(baos.toByteArray())))
      }
      Thread.sleep(50000)
    }
  }
}
