package TriForce

class Bridge {
  var code = ""
  var set = false

  def consume: String = {
    this.synchronized {
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
      notify()
    }
  }
}
