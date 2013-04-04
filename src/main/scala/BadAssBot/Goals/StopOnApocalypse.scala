package BadAssBot.Goals

import BadAssBot.{PossibleAction, InternalState, ExternalState, Goal}
import framework.{Heading, Move}


class StopOnApocalypse extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {
    if (externalState.isApocalypseComing)
      Some(PossibleAction(Move(Heading.Nowhere), 1000))
    else
      None
  }

}