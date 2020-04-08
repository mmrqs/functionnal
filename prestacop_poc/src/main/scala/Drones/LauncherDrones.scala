package Drones

import TriForce.{Bridge, Dispatcher, Producer}

object LauncherDrones extends App {
  var passerelle = new Bridge
  val producer1 = new Producer(1, passerelle).start()

  val passerelle2 = new Bridge
  val producer2 = new Producer(2, passerelle2).start()

  val passerelle3 = new Bridge
  val producer3 = new Producer(3, passerelle3).start()

  var pacerelleMap = Map(
    1 -> passerelle,
    2 -> passerelle2,
    3 -> passerelle3
  )
  var dispatcher = new Dispatcher(pacerelleMap).run()
}
