package BadAssBot.Goals

import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}
import framework.Explode
import scala.util.Random


case class Suicide(size: Int) extends Goal {


  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {

    val enemyInRange = externalState.view.nearestEnemyInRange(size - 1)

    if (enemyInRange.isDefined && externalState.energy < 600 && Random.nextDouble() < 0.5)
    {
      val effectiveRange = (enemyInRange.get.length.ceil.toInt + 1).max(size)

      Some(PossibleAction(Explode(effectiveRange), 200))
    }
    else
      None
    }

}


