import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Calendar, Date, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object MyProducer extends App {

  class Message(var id: Int, var time: String, var coords: (Int, Int))

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

  // new producer
  val producer = new KafkaProducer[String, String](props)

  // Periodic notifications simulation
  while(true) {
    val rnd = new scala.util.Random

    // we generate a random id between 1 and 10
    val id = start + rnd.nextInt( (end - start) + 1 )

    // Date
    val date: Date = Calendar.getInstance().getTime()
    val dateFormat: DateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
    val strDate: String = dateFormat.format(date)

    // Geographic coordinates
    val x = startC + rnd.nextInt( (endC - startC) + 1 )
    val y = startC + rnd.nextInt( (endC - startC) + 1 )

    producer.send(new ProducerRecord[String, String]("patrol", id.toString + "," + strDate +","+ x.toString +","+ y.toString))
    Thread.sleep(1000)
  }
}
