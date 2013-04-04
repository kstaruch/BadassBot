package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.Explode

case class Suicide(size: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    //TODO: map instead match
    val enemyInRange = externalState.view.nearestEnemyInRange(size - 1)

    if (enemyInRange.isDefined || externalState.notGonnaMakeItBeforeApocalypse)
      Some(PossibleAction(Explode(size), 200))
    else
      None
    }

}
