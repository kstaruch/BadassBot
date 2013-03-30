package BadAssBot.Goals

import BadAssBot.{PossibleAction, ExternalState, Goal}
import framework.Move

case class RunFromEnemy(range: Int) extends Goal {

  def evaluate(externalState: ExternalState): Option[PossibleAction] = {
    //TODO: map instead match
    externalState.view.nearestEnemyInRange(range) match {
      case Some(coords) => {
        //print("run ")
        Some(PossibleAction(Move((coords * -1).signum.toHeading), 10))
      }
      case _ => None
    }
  }
}
