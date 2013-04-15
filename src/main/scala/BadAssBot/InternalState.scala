package BadAssBot

import framework.Coord

object InternalState {

  def empty(): InternalState = {
    InternalState(Coord(0, 0), 0, 0, 0)
  }

}

case class InternalState(previousMove: Coord, reloadCounter: Int, age: Int, slideStart: Int) {

}
