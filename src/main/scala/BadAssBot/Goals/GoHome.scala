package BadAssBot.Goals

import BadAssBot.{PossibleAction, ExternalState, Goal}
import framework.{Heading, Move}

case class GoHome(energyThreshold: Int) extends Goal {
  def evaluate(externalState: ExternalState): Option[PossibleAction] = {
    externalState match {
      case s if s.isSlave && s.energy > energyThreshold
      => Some(PossibleAction(Move(Heading(s.master.x, s.master.y)), s.energy))
      case _ => None
    }
  }
}
