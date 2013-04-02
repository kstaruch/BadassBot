package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.{Heading, Move}
import util.Random

case class GoHome(energyThreshold: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    externalState match {
      case s if s.isSlave && s.energy > energyThreshold
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), s.energy))
      case s if s.isSlave && externalState.isApocalypseComing
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), 100))
      case _ => None
    }
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
