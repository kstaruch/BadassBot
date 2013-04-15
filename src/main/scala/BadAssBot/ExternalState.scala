package BadAssBot

import framework.{NoOp, Coord, View}
import scala.util.Random

case class ExternalState(generation: Int, name: String, time: Int, apocalypse: Int, view: View,
                         energy: Int, master: Coord, previousMove: Coord, reloadCounter: Int,
                         maxSlaves: Int, slaves: Int,
                         state: Map[String, String]) {


  def remainsBeforeApocalypse = apocalypse - time

  def slaveLimitReached: Option[PossibleAction] = slaves < maxSlaves match {
    case true => None
    case false => Some(PossibleAction(NoOp(), 1))
  }


  def isApocalypseComing: Boolean = {

    val remains = apocalypse - time
    remains < 100 //just because
  }

  def notGonnaMakeItBeforeApocalypse: Boolean = {
    val remains = apocalypse - time
    remains < 5
  }


  val isReadyToFire = Random.nextDouble() < 0.2
  val isSlave = generation > 0
}