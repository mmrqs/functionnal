package Drones

import TriForce.{Bridge, Dispatcher, Producer}

object LauncherDrones extends App {
  var pacerelle = new Bridge
  val producer1 = new Producer(1, pacerelle).start()

  var pacerelleMap = Map(
    1 -> pacerelle
  )
  var dispatcher = new Dispatcher(pacerelleMap).run()
}
