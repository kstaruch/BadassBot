package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.Spawn

case class EnemyProximityMinionSpawn(range: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    //TODO: map instead match
    externalState.view.nearestEnemyInRange(range) match {
      case Some(coords) if externalState.isReadyToFire => {
        //print("fire ")
        Some(PossibleAction(Spawn(direction = coords.signum.toHeading, energy = 100), 1))
      }
      case _ => None
    }
  }
}