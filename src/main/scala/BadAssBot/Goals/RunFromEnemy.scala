package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.Move

case class RunFromEnemy(range: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    //TODO: map instead match
    externalState.view.nearestEnemyInRange(range) match {
      case Some(coords) =>
        Some(PossibleAction(Move((coords * -1).signum.toHeading), 100))
      case _ => None
    }
  }
}
