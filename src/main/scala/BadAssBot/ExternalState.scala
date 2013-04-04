package BadAssBot

import framework.{Coord, View}

case class ExternalState(generation: Int, name: String, time: Int, apocalypse: Int, view: View,
                         energy: Int, master: Coord, previousMove: Coord, reloadCounter: Int, internalStateSerialized: String) {

  def isApocalypseComing: Boolean = {

    val remains = apocalypse - time
    remains < 200 //just because
  }

  def notGonnaMakeItBeforeApocalypse: Boolean = {
    val remains = apocalypse - time
    remains < 10
  }


  val isReadyToFire = reloadCounter == 0
  val isSlave = generation > 0
}