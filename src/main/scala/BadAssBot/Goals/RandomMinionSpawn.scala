package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import util.Random
import framework.{Heading, Spawn}

case class RandomMinionSpawn(randomSpawnChance: Double) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    Random.nextDouble() match {
      case x if x < randomSpawnChance && externalState.energy > 200 && !externalState.isApocalypseComing
        => Some(PossibleAction(Spawn(direction = Heading.random, energy = 100), 1))
      case _ => None
    }
  }
}
