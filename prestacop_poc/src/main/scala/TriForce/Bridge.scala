package TriForce

class Bridge {
  var code = ""
  var set = false
  var requestHelp = false

  def consume: String = {
    this.synchronized {
      requestHelp = true
      while(!set) wait()
      set = false
      return code
    }
  }

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
