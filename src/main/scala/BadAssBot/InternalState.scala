package BadAssBot

import framework.Coord

object InternalState {

  def empty(): InternalState = {
    InternalState(Coord(0, 0), 0, None)
  }

  def fromXML(node: scala.xml.Node): InternalState =
    InternalState(Coord.parse(
      (node \ "previousMove").text),
      (node \ "reloadCounter").text.toInt,
      Some((node \ "apocalypse").text.toInt))

}

case class InternalState(previousMove: Coord, reloadCounter: Int, apocalypse: Option[Int]) {

  def isAlmostApocalypse(time: Int): Boolean = {
    val remains = apocalypse.getOrElse(5000) - time

    remains < 300
  }



  def toXML =
    <state>
      <previousMove>{previousMove.toString}</previousMove>
      <reloadCounter>{reloadCounter}</reloadCounter>
      <apocalypse>{apocalypse.getOrElse(5000)}</apocalypse>
    </state>

}
