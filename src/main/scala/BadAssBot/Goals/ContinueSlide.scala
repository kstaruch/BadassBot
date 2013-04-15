package BadAssBot.Goals

import BadAssBot.{PossibleAction, InternalState, ExternalState, Goal}
import framework.Move

case class ContinueSlide(numberOfSteps: Int) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState) = {
    (externalState.time - internalState.slideStart) match {
      case x if internalState.age > 5 && x < numberOfSteps
      => Some(PossibleAction(Move(internalState.previousMove.toHeading), 500))
      case _ => None
    }
  }

}
