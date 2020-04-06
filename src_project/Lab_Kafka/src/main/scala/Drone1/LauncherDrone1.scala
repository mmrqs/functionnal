package Drone1

object LauncherDrone1 extends App {
  var pacerelle = new Bridge
  val producer1 = new Producer(1, pacerelle).start()
  val consumer1 = new Consumer(1, pacerelle).start()
}
