package BadAssBot.Goals

import BadAssBot.{PossibleAction, InternalState, ExternalState, Goal}
import framework.Explode

case class SuicideBeforeApocalypse(timeBefore: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState) = {

    externalState.isApocalypseComing match {
      case true if externalState.remainsBeforeApocalypse < timeBefore => Some(PossibleAction(Explode(5), 200))
      case _ => None
    }
  }
}