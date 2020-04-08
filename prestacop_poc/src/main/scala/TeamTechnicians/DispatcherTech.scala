package TeamTechnicians

import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

import TeamTechnicians.Technicians.BridgeTech

import scala.collection.JavaConverters._

class DispatcherTech (var bridgesTech : Seq[BridgeTech] ) extends Thread {
  override def run () {
    val props = new Properties ()
    props.put ("bootstrap.servers", "localhost:9092")
    props.put ("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put ("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put ("auto.offset.reset", "latest")
    props.put ("group.id", "consumer-group")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String] (props)
    consumer.subscribe (util.Arrays.asList ("SOS-SENDER") )

    while (true) {
      val record = consumer.poll (1000).asScala
      for (data <- record.iterator) {
        val string = data.value ()

        // Dispatch the alert to the least busy employee
        whoIsBusy.produce(string)
      }
    }
  }

  def whoIsBusy: BridgeTech = {
    var lazyTechTasks = bridgesTech(0).amIBusy
    var lazyTech = bridgesTech(0)

    for(bridge <- bridgesTech) {
      if (bridge.amIBusy < lazyTechTasks) {
        lazyTech = bridge
        lazyTechTasks = bridge.amIBusy
      }
    }
    return lazyTech
  }
}