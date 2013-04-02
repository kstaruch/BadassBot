package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.Explode

case class Suicide(size: Int) extends Goal {
  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    //TODO: map instead match
    externalState.view.nearestEnemyInRange(size - 1) match {
      case Some(coords) => Some(PossibleAction(Explode(size), 200))
      case None => None
    }
  }
}
