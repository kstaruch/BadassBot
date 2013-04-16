package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.{SpawnWithRole, Spawn}
import scala.util.Random

case class EnemyProximityMinionSpawn(range: Int, spawnProbability: Double) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    externalState.slaveLimitReached orElse fireIfEnemyInRange(externalState)
  }

  protected def fireIfEnemyInRange(externalState: ExternalState): Option[PossibleAction] = {

    externalState.view.nearestEnemyInRange(range) match {
      case Some(coords) if externalState.isReadyToFire && Random.nextDouble() < spawnProbability => { //TODO: this can be optimised by outering prob check
        Some(PossibleAction(SpawnWithRole(direction = coords.signum.toHeading, energy = 100, role = "missile"), 200))
      }
      case _ => None
    }
  }
}