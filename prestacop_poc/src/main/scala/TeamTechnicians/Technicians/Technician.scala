package TeamTechnicians.Technicians

import Utils.Constants

class Technician (var id : Int, var bridge : BridgeTech, var bridgeBigBoss: BridgeTech) extends Thread {

  override def run(): Unit = {

    while(true) {

      // he receives the task
      var string = bridge.consume.split(",")

      // he performs the task
      val rnd = new scala.util.Random
      val natureAlert = Constants.possibleAlerts.toSeq(
        rnd.nextInt(Constants.possibleAlerts.size)
      )
      Thread.sleep(20000)
      bridgeBigBoss.produce(id.toString + "," + string(0)+ "," + natureAlert._1)
    }
  }
}
