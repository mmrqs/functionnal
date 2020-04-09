package Model

import java.text.SimpleDateFormat

class Ticket(droneId : Int, date: Long, violationCode: Int, latitude: Float, longitude: Float, imageId: String) {
  def this(droneId: Int, data: Array[String], imageId: String, simpleDateFormat: SimpleDateFormat) = this(
    droneId,
    //date
    data.lift(4).map(d => {
      try {
        simpleDateFormat.parse(d).getTime
      } catch {
        case _ => 0
      }
    }).get,
    //violation code
    data.lift(5).map(vc => {
      try {
        vc.toInt
      } catch {
        case _ => 0
      }
    }).get,
    //latitude
    data.lift(43).getOrElse("0").toFloat,
    data.lift(44).getOrElse("0").toFloat,
    imageId
  )
  def this(droneId: Int, data: Array[String], simpleDateFormat: SimpleDateFormat) = this(droneId, data, "none", simpleDateFormat)

  def this(data: Array[String]) = this(
    data.apply(0).toInt,
    data.apply(1).toLong,
    data.apply(2).toInt,
    data.apply(3).toFloat,
    data.apply(4).toFloat,
    data.apply(5)
  )

  override def toString: String = {
    droneId + "," + date + "," + violationCode + "," + latitude + "," + longitude + "," + imageId
  }
}
