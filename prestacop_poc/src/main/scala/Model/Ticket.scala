package com.spark.project

class Ticket(alert: Int, data : List[String]){
  val Alert : Int = alert
  val Summons_Number: Option[Float] = toFloat(data(0))
  val Plate_ID: Option[String] = data.lift(1)
  val Registration_State:Option[String] = data.lift(2)
  val Plate_Type:Option[String] = data.lift(3)
  val Issue_Date: Option[String] = data.lift(4)
  val Violation_Code:Option[Float] = toFloat(data(5))
  val Vehicle_Body_Type:Option[String] = data.lift(6)
  val Vehicle_Make:Option[String] = data.lift(7)
  val Issuing_Agency:Option[String] = data.lift(8)
  val Street_Code1: Option[Float] = toFloat(data(9))
  val Street_Code2: Option[Float] = toFloat(data(10))
  val Street_Code3: Option[Float] = toFloat(data(11))
  val Vehicle_Expiration_Date:Option[Float] = toFloat(data(12))
  val Violation_Location:Option[Float] = toFloat(data(13))
  val Violation_Precinct:Option[Float] = toFloat(data(14))
  val Issuer_Precinct:Option[Float] = toFloat(data(15))
  val Issuer_Code:Option[Float] = toFloat(data(16))
  val Issuer_Command:Option[Float] = toFloat(data(17))
  val Issuer_Squad:Option[Float] = toFloat(data(18))
  val Violation_Time: Option[String] = data.lift(19)
  val Time_First_Observed: Option[Any] = data.lift(20)
  val Violation_County:Option[Any] = data.lift(21)
  val Violation_In_Front_Of_Or_Opposite:Option[String] = data.lift(22)
  val House_Number:Option[Float] = toFloat(data(23))
  val Street_Name: Option[String] = data.lift(24)
  val Intersecting_Street:Option[Any] = data.lift(25)
  val Date_First_Observed:Option[Float] = toFloat(data(26))
  val Law_Section:Option[Float] = toFloat(data(27))
  val Sub_Division:Option[String] = data.lift(28)
  val Violation_Legal_Code:Option[Any] = data.lift(29)
  val Days_Parking_In_Effect:Option[Any] = data.lift(30)
  val From_Hours_In_Effect:Option[Any] = data.lift(31)
  val To_Hours_In_Effect:Option[Any] = data.lift(32)
  val Vehicle_Color:Option[String] = data.lift(33)
  val Unregistered_Vehicle:Option[Float] = toFloat(data(34))
  val Vehicle_Year:Option[Float] = toFloat(data(35))
  val Meter_Number:Option[Any] = data.lift(36)
  val Feet_From_Curb:Option[Float] = toFloat(data(37))
  val Violation_Post_Code:Option[Any] = data.lift(38)
  val Violation_Description:Option[Any] = data.lift(39)
  val No_Standing_or_Stopping_Violation:Option[Any] = data.lift(40)
  val Hydrant_Violation:Option[Any] = data.lift(41)
  val Double_Parking_Violation:Option[Any] = data.lift(42)
  val Latitude:Option[Any] = data.lift(43)
  val Longitude:Option[Any] = data.lift(44)
  val Community_Board:Option[Any] = data.lift(45)
  val Community_Council:Option[Any] = data.lift(46)
  val Census_Tract:Option[Any] = data.lift(47)
  val BIN:Option[Any] = data.lift(48)
  val BBL:Option[Any] = data.lift(49)
  val NTA:Option[Any] = data.lift(50)

  def toFloat(s: String): Option[Float] = {
    try {
      Some(s.toFloat)
    } catch {
      case e: Exception => None
    }
  }

  override def toString: String = {
    super.toString  + "," + Alert.toString  + "," + Summons_Number.toString  + "," + Plate_ID.toString  + "," + Registration_State.toString  + "," + Plate_Type.toString  + "," + Issue_Date.toString  + "," + Violation_Code.toString  + "," + Vehicle_Body_Type.toString  + "," + Vehicle_Make.toString  + "," + Issuing_Agency.toString  + "," + Street_Code1.toString  + "," + Street_Code2.toString  + "," + Street_Code3.toString  + "," + Vehicle_Expiration_Date.toString  + "," + Violation_Location.toString  + "," + Violation_Precinct.toString  + "," + Issuer_Precinct.toString  + "," + Issuer_Code.toString  + "," + Issuer_Command.toString  + "," + Issuer_Squad.toString  + "," + Violation_Time.toString  + "," + Time_First_Observed.toString  + "," + Violation_County.toString  + "," + Violation_In_Front_Of_Or_Opposite.toString  + "," + House_Number.toString  + "," + Street_Name.toString  + "," + Intersecting_Street.toString  + "," + Date_First_Observed.toString  + "," + Law_Section.toString  + "," + Sub_Division.toString  + "," + Violation_Legal_Code.toString  + "," + Days_Parking_In_Effect.toString  + "," + From_Hours_In_Effect.toString  + "," + To_Hours_In_Effect.toString  + "," + Vehicle_Color.toString  + "," + Unregistered_Vehicle.toString  + "," + Vehicle_Year.toString  + "," + Meter_Number.toString  + "," + Feet_From_Curb.toString  + "," + Violation_Post_Code.toString  + "," + Violation_Description.toString  + "," + No_Standing_or_Stopping_Violation.toString  + "," + Hydrant_Violation.toString  + "," + Double_Parking_Violation.toString  + "," + Latitude.toString  + "," + Longitude.toString  + "," + Community_Board.toString  + "," + Community_Council.toString  + "," + Census_Tract.toString  + "," + BIN.toString  + "," + BBL.toString  + "," + NTA.toString
  }
}
