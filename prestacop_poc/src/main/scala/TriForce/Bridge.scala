package TriForce

/**
 * allows the Dispatcher and the Producer to communicate
 */

class Bridge {
  var code = ""
  var set = false
  var requestHelp = false

  /**
   * Request help
   * until the tech has help, wait
   * Consume the message sent by the Dispatcher
   * @return
   */
  def consume: String = {
    this.synchronized {
      requestHelp = true
      while(!set) wait()
      set = false
      return code
    }
  }

  /**
   * produce a message for the drone
   * @param codeNumber
   */
  def produce(codeNumber : String): Unit = {
    this.synchronized {
      while(set) wait()
      code = codeNumber
      set = true
      requestHelp = false
      notify()
    }
  }

  def helpRequested: Boolean = {
    requestHelp
  }
}
