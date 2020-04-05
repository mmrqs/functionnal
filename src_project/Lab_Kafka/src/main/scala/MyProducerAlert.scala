import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Calendar, Date, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object MyProducerAlert extends App {

  class Message(var id: Int, var time: String, var coords: (Int, Int))

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")


  // new producer
  val producer = new KafkaProducer[String, String](props)

  // bornes random id
  val start = 1
  val end   = 10

  // bornes coordinates
  val startC = 1
  val endC = 100

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

  // Alert notifications simulation
  while(true) {

    val rnd = new scala.util.Random

    //we generate a random id between 1 and 10
    val id = start + rnd.nextInt( (end - start) + 1 )

    //we generate a random alert
    val natureAlert = possibleAlerts(
      rnd.nextInt(possibleAlerts.length)
    )

    //Date
    val date: Date = Calendar.getInstance().getTime()
    val dateFormat: DateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
    val strDate: String = dateFormat.format(date)

    // Geographic coordinates
    val x = startC + rnd.nextInt( (endC - startC) + 1 )
    val y = startC + rnd.nextInt( (endC - startC) + 1 )

    producer.send(new ProducerRecord[String, String]("alert",
      id.toString + "," + strDate +","+ x.toString +","+ y.toString + "," + natureAlert))
    Thread.sleep(10000)
  }
}
