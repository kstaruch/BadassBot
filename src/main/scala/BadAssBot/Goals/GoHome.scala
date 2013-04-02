package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.{Heading, Move}
import util.Random

case class GoHome(energyThreshold: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    externalState match {
      case s if s.isSlave && s.energy > energyThreshold
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), s.energy))
      case s if s.isSlave && apocalypseIsComing(s.master.distanceTo(s.view.center), internalState.apocalypse.getOrElse(5000), externalState.time)
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), 100))
      case _ => None
    }
  }

  def apocalypseIsComing(distance: Double, apocalypse: Int, time: Int): Boolean = {
    val remains = apocalypse - time

    remains < (distance * 0.75)
  }

}

case class BecomeMine(range: Int, mineFactor: Double) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {

   externalState.view.nearestEnemyInRange(range) match {
     case None if Random.nextDouble() < mineFactor => {
       Some(PossibleAction(Move(Heading.Nowhere), 100))
     }
     case _ => None
   }
  }
}
