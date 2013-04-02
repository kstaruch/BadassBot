package BadAssBot

import framework.{Coord, View}

case class ExternalState(generation: Int, name: String, time: Int, apocalypse: Option[Int], view: View,
                         energy: Int, master: Coord, previousMove: Coord, reloadCounter: Int, internalStateSerialized: String) {



  val isReadyToFire = reloadCounter == 0
  val isSlave = generation > 0
}