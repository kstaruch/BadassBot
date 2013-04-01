package BadAssBot.Goals

import BadAssBot.{PossibleAction, ExternalState, Goal}
import framework.{Heading, Move}
import util.Random

case class GoHome(energyThreshold: Int) extends Goal {

  def evaluate(externalState: ExternalState): Option[PossibleAction] = {
    externalState match {
      case s if s.isSlave && s.energy > energyThreshold
      => {
        //print("gohome ")
        Some(PossibleAction(Move(Heading(s.master.x.signum, s.master.y.signum)), s.energy))
      }
      case _ => None
    }
  }
}

case class BecomeMine(range: Int, mineFactor: Double) extends Goal {

  def evaluate(externalState: ExternalState): Option[PossibleAction] = {

   externalState.view.nearestEnemyInRange(range) match {
     case None if Random.nextDouble() < mineFactor => {
       println("mine")
       Some(PossibleAction(Move(Heading.Nowhere), 100))
     }
     case _ => None
   }
  }
}
