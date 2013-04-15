package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.{Heading, Coord, Move}
import util.Random

case class GoHome(energyThreshold: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {

    val moveTowardsMaster = Heading(externalState.master.x.signum, externalState.master.y.signum)

    externalState match {
      case s if s.isSlave && s.energy > energyThreshold && externalState.view.canBeMovedInDirection(moveTowardsMaster)
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), s.energy / 5))
      case s if s.isSlave && externalState.isApocalypseComing
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), 100))
      case s if s.isSlave && s.master.length < 10 && s.energy > energyThreshold / 2 && externalState.view.canBeMovedInDirection(moveTowardsMaster)
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), s.energy))
      case s if s.isSlave && internalState.age > 10 && internalState.previousMove == Coord(0, 0)
        => Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), 100))
      case _ => None
    }
  }
}


