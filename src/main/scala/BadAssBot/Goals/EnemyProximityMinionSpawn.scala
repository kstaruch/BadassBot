package BadAssBot.Goals

import BadAssBot.{PossibleAction, ExternalState, Goal}
import framework.Spawn

case class EnemyProximityMinionSpawn(range: Int) extends Goal {

  def evaluate(externalState: ExternalState): Option[PossibleAction] = {
    //TODO: map instead match
    externalState.view.nearestEnemyInRange(range) match {
      case Some(coords) if externalState.isReadyToFire =>
        Some(PossibleAction(Spawn(direction = coords.signum.toHeading, energy = 100), 1))
      case None => None
    }
  }
}