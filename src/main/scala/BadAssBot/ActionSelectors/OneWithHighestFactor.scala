package BadAssBot.ActionSelectors

import BadAssBot.{ActionSelector, PossibleAction}
import util.Random

object ActionSelector {

  def apply() = new OneWithHighestFactor
  //def apply() = new Proportional
}

class OneWithHighestFactor extends ActionSelector {

  def selectOneOf(actions: Seq[PossibleAction]): Option[PossibleAction] = {
    actions match {
      case Seq() => None
      case s  => Some(s.maxBy(p => p.factor))
    }
  }
}

class Proportional extends ActionSelector {

  def selectOneOf(actions: Seq[PossibleAction]): Option[PossibleAction] = {

    def chooseProportional(accSum: Double, remainingActions: Seq[PossibleAction]) : Option[PossibleAction] = {
      remainingActions match {
        case Nil => None
        case h :: tail if accSum < h.factor => Some(h)
        case h :: tail => chooseProportional(accSum - h.factor, tail)
      }
    }

    val sum = actions.foldLeft(0.0)(_ + _.factor)

    val rnd = Random.nextDouble() * sum

    chooseProportional(rnd, actions)

  }

}
