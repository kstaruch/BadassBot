package BadAssBot

import framework.{Coord, View}

case class ExternalState(generation: Int, name: String, time: Int, view: View,
                         energy: Int, master: Coord, previousMove: Coord, reloadCounter: Int) {
  val isReadyToFire = reloadCounter == 0
  val isSlave = generation > 0
}