package BadAssBot.ActionSelectors

import BadAssBot.{ActionSelector, PossibleAction}

object OneWithHighestFactor {
  def apply() = new OneWithHighestFactor
}

class OneWithHighestFactor extends ActionSelector {
  def selectOneOf(actions: Seq[PossibleAction]): Option[PossibleAction] = {
    actions match {
      case Seq() => None
      case s  => Some(s.maxBy(p => p.factor))
    }
  }
}
