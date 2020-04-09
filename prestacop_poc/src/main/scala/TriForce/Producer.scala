package TriForce

import java.io.{ByteArrayOutputStream, File}
import java.util.{Base64, Calendar, Date, Properties}

import Model.Ticket
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
      val x = Constants.startC + rnd.nextInt( (Constants.endC - Constants.startC) + 1 ).toLong
      val y = Constants.startC + rnd.nextInt( (Constants.endC - Constants.startC) + 1 ).toLong

      var caseD: Int = 0
      val m = rnd.nextInt(100)
      m match {
        case m if m <= 75 => caseD = 1 // Checkpoint
        case m if m > 75 && m <= 99 => caseD = 2 // Alert
        case m if m > 99 => caseD = 3 // Intervention
      }
      
     // PREPARE DATA
      // Date
      val date: Date = Calendar.getInstance().getTime

      caseD match {
        // PERIODIC NOTIFICATION
        case 1 => producer.send(new ProducerRecord[String, String]("PERIODIC", id.toString + "," + date.getTime + "," + x + "," + y + "," + rnd.nextInt(100).toFloat / 100))
        // ALERT
        case 2 => {
          val natureAlert = Constants.possibleAlerts.toSeq(
            rnd.nextInt(Constants.possibleAlerts.size)
          )
          producer.send(new ProducerRecord[String, String](topicAlert,
            new Ticket(id, date.getTime, natureAlert._1.toInt, x, y, id + "-" + date.getTime + "-" + natureAlert._1).toString))

          var baos = new ByteArrayOutputStream();
          ImageIO.write(ImageIO.read(new File(Constants.possibleImages(rnd.nextInt(Constants.possibleImages.size)))),
            "jpg", baos);

          producer.send(new ProducerRecord[String, String](topicImages, id.toString + "-" + date.getTime + "-" + natureAlert._1 + ","
            +Base64.getEncoder().encodeToString(baos.toByteArray())))

        }
        // HUMAN INTERVENTION
        case _ => producer.send(new ProducerRecord[String, String](topicSendHelp,
          id.toString + "," + date + "," + x.toString +","+ y.toString + "," + Constants.picture))

          val codeP = pacerelle.consume

          producer.send(new ProducerRecord[String, String](topicAlert,
            new Ticket(id, date.getTime, codeP.toInt, x, y, id + "-" + date + "-" + codeP.toInt).toString))

      var baos = new ByteArrayOutputStream();
          ImageIO.write(ImageIO.read(new File(Constants.possibleImages(rnd.nextInt(Constants.possibleImages.size)))),
            "jpg", baos);
          producer.send(new ProducerRecord[String, String](topicImages, id.toString + "-" + date.getTime + "-" + codeP + ","
            +Base64.getEncoder().encodeToString(baos.toByteArray())))
      }
      Thread.sleep(1000)
    }
  }
}
