package TeamTechnicians.BigBoss

import java.util.Properties

import TeamTechnicians.DispatcherTech
import TeamTechnicians.Technicians.{BridgeTech, Technician}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.StdIn.readLine
import scala.util.control.Breaks.{break, breakable}

object BigBoss extends App {

  //launches bridges for techs
  val bridgeTech1 = new BridgeTech
  val bridgeTech2 = new BridgeTech
  val bridgeTech3 = new BridgeTech

  val bridges = Seq(bridgeTech1, bridgeTech2, bridgeTech3)

  //launches DispatcherTech
  val dispatcherTech = new DispatcherTech(bridges).start()

  val bridgeBigBoss = new BridgeTech
  //launches 3 techs
  val tech1 = new Technician(1, bridgeTech1, bridgeBigBoss).start()
  val tech2 = new Technician(2, bridgeTech2, bridgeBigBoss).start()
  val tech3 = new Technician(3, bridgeTech3, bridgeBigBoss).start()

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  // new producer
  val producer = new KafkaProducer[String, String](props)

  // Periodic notifications simulation
  while(true) {
    println("------------------------")
    var toCheck = bridgeBigBoss.consume.split(",")
    println("Tech " + toCheck(0) + " input : " + toCheck(2) + " for drone ID : " + toCheck(1))

    breakable {
      while (true) {
        println("Do you confirm?  y/n")
        val answer = readLine()
        if (answer == "y") {
          producer.send(new ProducerRecord[String, String]("SOS-RESPONSE", toCheck(1) + "," +
            toCheck(2)))
          break
        } else if (answer == "n") {
          print("Drone id :")
          val droneID = readLine()
          print("CODE : ")
          val CODE = readLine()
          producer.send(new ProducerRecord[String, String]("SOS-RESPONSE", droneID.toString + "," +
            CODE.toString))
          println("Response sent")
          break
        }
      }
    }
  }
}
