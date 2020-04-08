package TeamTechnicians.Technicians

import scala.collection.mutable

class BridgeTech {
  var toDo = new mutable.Queue[String]()

  def consume: String = {
    this.synchronized {
      while(amIBusy == 0) wait()
      return toDo.dequeue
    }
  }

  def produce(task : String): Unit = {
    this.synchronized {
      toDo.enqueue(task)
      notify()
    }
  }

  def amIBusy: Int = {
    this.synchronized {
      return toDo.size
    }
  }

}
