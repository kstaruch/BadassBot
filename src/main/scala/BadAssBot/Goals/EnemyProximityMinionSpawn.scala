package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.Spawn

case class EnemyProximityMinionSpawn(range: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    externalState.slaveLimitReached orElse fireIfEnemyInRange(externalState)
  }

  protected def fireIfEnemyInRange(externalState: ExternalState): Option[PossibleAction] = {

    externalState.view.nearestEnemyInRange(range) match {
      case Some(coords) if externalState.isReadyToFire => {
        Some(PossibleAction(Spawn(direction = coords.signum.toHeading, energy = 100), 100))
      }
      case _ => None
    }
  }
}