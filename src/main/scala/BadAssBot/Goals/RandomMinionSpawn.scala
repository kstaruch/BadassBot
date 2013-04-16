package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import util.Random
import framework.{Heading, Spawn}

/**
 * A strategy of spawning mini-bots with a given probability as long as energy is sufficient
 */
case class RandomMinionSpawn(randomSpawnChance: Double) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    externalState.slaveLimitReached orElse spawnMinion(externalState)
  }

  protected def spawnMinion(externalState: ExternalState): Option[PossibleAction] = {
    Random.nextDouble() match {
      case x if x < randomSpawnChance && externalState.energy > 200 && !externalState.isApocalypseComing
      => Some(PossibleAction(Spawn(direction = Heading.random, energy = 100), 1))
      case _ => None
    }
  }
}
