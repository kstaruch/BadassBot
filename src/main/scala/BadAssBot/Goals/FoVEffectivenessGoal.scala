package BadAssBot.Goals

import framework._
import scala.Some
import framework.Move
import util.Random
import BadAssBot.{InternalState, PossibleAction, ExternalState, Goal}

class FoVEffectivenessGoal(heading: Heading) extends Goal {

  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction] = {

    val sumOfCells = externalState.view.coordsInDirectionOf(heading)
      .foldLeft(0.0)(_ + calculateEffectivenessOf(_, externalState))

    Some(PossibleAction(Move(heading), sumOfCells))
  }

  def calculateEffectivenessOf(coord: Coord, externalState: ExternalState): Double = {

    val distance = externalState.view.Relative.fromAbsolute(coord).stepCount

    externalState.isSlave match {
      case true => calculateEffectivenessForSlave(coord, externalState.view, distance)
      case false => calculateEffectivenessForMaster(coord, externalState.view, distance)
    }
  }

  protected def calculateEffectivenessForMaster(coord: Coord, view: View, distance: Int): Double = {
    view.Absolute(coord) match {
      case Cell.Enemy if distance < 2 => -1000
      case Cell.Enemy if distance >= 2 => -400 / distance
      case Cell.EnemySlave => -100 / distance
      case Cell.GoodBeast if distance < 2 => 1000
      case Cell.GoodBeast if distance >= 2 => 600 / distance
      case Cell.BadBeast if distance <= 2 => -1000
      case Cell.BadBeast if distance > 2 => -150 / distance
      case Cell.BadPlant if distance < 2 => -1000
      case Cell.GoodPlant if distance < 2 => 500
      case Cell.GoodPlant if distance >= 2 => 500 / distance
      case Cell.Wall if distance < 2 => -1000
      case Cell.YourSlave => 500 / distance
      case _ => 0.0
    }
  }

  protected def calculateEffectivenessForSlave(coord: Coord, view: View, distance: Int): Double = {
    view.Absolute(coord) match {
      case Cell.Enemy if distance < 10 => 1000
      case Cell.EnemySlave => 500 / distance
      case Cell.GoodBeast if distance < 2 => 600
      case Cell.GoodBeast if distance >= 2 => 600 / distance
      case Cell.BadBeast if distance <= 4 => -400
      case Cell.BadBeast if distance > 4 => -150 / distance
      case Cell.BadPlant if distance < 2 => -100
      case Cell.GoodPlant if distance < 2 => 500
      case Cell.GoodPlant if distance >= 2 => 500 / distance
      case Cell.Wall if distance < 2 => -1000
      //case Cell.Wall if distance >= 2 => (-10 / distance) - Random.nextInt(5)
      case Cell.YourSlave if distance <= 3 => -1000
      case Cell.YourSlave if distance > 3 && distance < 15 => 10 / distance
      case Cell.You => -500 / distance
      case _ => 0.0
    }
  }
}

object MN  extends FoVEffectivenessGoal(Heading.North) {}
object MNE extends FoVEffectivenessGoal(Heading.NorthEast) {}
object ME  extends FoVEffectivenessGoal(Heading.East) {}
object MSE extends FoVEffectivenessGoal(Heading.SouthEast) {}
object MS  extends FoVEffectivenessGoal(Heading.South) {}
object MSW extends FoVEffectivenessGoal(Heading.SouthWest) {}
object MW  extends FoVEffectivenessGoal(Heading.West) {}
object MNW extends FoVEffectivenessGoal(Heading.NorthWest) {}





