package BadAssBot.ActionSelectors

import BadAssBot.{PossibleAction, ActionSelector}
import scala.util.Random

case class Proportional(top: Int) extends ActionSelector {

  def selectOneOf(actions: Seq[PossibleAction]): Option[PossibleAction] = {

    def chooseProportional(accSum: Double, remainingActions: Seq[PossibleAction]) : Option[PossibleAction] = {
      remainingActions match {
        case Nil => None
        case h :: tail if accSum < h.factor => Some(h)
        case h :: tail => chooseProportional(accSum - h.factor, tail)
      }
    }

    val topResults = actions.sortWith(_.factor > _.factor).take(top)
    val sum = topResults.foldLeft(0.0)(_ + _.factor)
    val rnd = Random.nextDouble() * sum

    chooseProportional(rnd, topResults)
  }

}
