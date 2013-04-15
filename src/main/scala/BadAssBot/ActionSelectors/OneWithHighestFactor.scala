package BadAssBot.ActionSelectors

import BadAssBot.{PossibleAction, ActionSelector}

class OneWithHighestFactor extends ActionSelector {

  def selectOneOf(actions: Seq[PossibleAction]): Option[PossibleAction] = {
    actions match {
      case Seq() => None
      case s  => Some(s.maxBy(p => p.factor))
    }
  }
}


