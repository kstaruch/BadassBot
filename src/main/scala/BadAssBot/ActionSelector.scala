package BadAssBot

import BadAssBot.ActionSelectors.{Proportional, OneWithHighestFactor}

trait ActionSelector {

  def selectOneOf(actions: Seq[PossibleAction]): Option[PossibleAction]
}

object ActionSelector {

  def apply() = new OneWithHighestFactor
  //def apply() = new Proportional(2)
}

