package BadAssBot.Goals

import BadAssBot.{PossibleAction, ExternalState, Goal}
import util.Random
import framework.{Heading, Spawn}

case class RandomMinionSpawn(randomSpawnChance: Double) extends Goal {

  def evaluate(externalState: ExternalState): Option[PossibleAction] = {
    Random.nextDouble() match {
      case x if x < randomSpawnChance && externalState.energy > 1000
        => Some(PossibleAction(Spawn(direction = Heading.random, energy = 100), 1))
      case _ => None
    }
  }
}
