package BadAssBot.Goals

import BadAssBot.{PossibleAction, InternalState, ExternalState, Goal}
import framework.Move

/**
 * A strategy of choosing a path towards an enemy - should be chosen only by explicit missile=bots
 */
case class SeekEnemy(range: Int) extends  Goal {
  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {

    isMissile(internalState) match {
      case true => externalState.view.nearestEnemyInRange(range).
        map(c => (PossibleAction(Move((c).signum.toHeading), 500)))
      case false => None
    }
  }

  def isMissile(internalState: InternalState) = internalState.role == "missile" //TODO: add explicit role classes
}
