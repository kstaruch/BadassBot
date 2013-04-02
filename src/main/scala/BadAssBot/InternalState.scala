package BadAssBot

import framework.Coord

object InternalState {

  def empty(): InternalState = {
    InternalState(Coord(0, 0), 0)
  }

  def fromXML(node: scala.xml.Node): InternalState =
    InternalState(Coord.parse(
      (node \ "previousMove").text),
      (node \ "reloadCounter").text.toInt)

}

case class InternalState(previousMove: Coord, reloadCounter: Int) {

  def toXML =
    <state>
      <previousMove>{previousMove.toString}</previousMove>
      <reloadCounter>{reloadCounter}</reloadCounter>
    </state>

}
