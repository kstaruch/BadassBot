package BadAssBot.Goals

import framework._
import scala.Some
import framework.Move
import util.Random
import BadAssBot.{PossibleAction, ExternalState, Goal}

class FoVEffectivenessGoal(heading: Heading) extends Goal {

  def evaluate(externalState: ExternalState): Option[PossibleAction] = {

    val sumOfCells = externalState.view.coordsInDirectionOf(heading)
      .foldLeft(0.0)(_ + calculateEffectivenessOf(_, externalState))

    Some(PossibleAction(Move(heading), sumOfCells))
  }

  def calculateEffectivenessOf(coord: Coord, externalState: ExternalState): Double = {

    externalState.isSlave match {
      case true => calculateEffectivenessForSlave(coord, externalState.view, coord.stepsTo(externalState.view.center))
      case false => calculateEffectivenessForMaster(coord, externalState.view, coord.stepsTo(externalState.view.center))
    }
  }

  protected def calculateEffectivenessForMaster(coord: Coord, view: View, distance: Int): Double = {
    view.Absolute(coord) match {
      case Cell.Enemy if distance < 2 => -1000
      case Cell.EnemySlave => -100 / distance
      case Cell.GoodBeast if distance < 2 => 600
      case Cell.GoodBeast if distance >= 2 => 600 / distance//(150 - distance * 10).max(10)
      case Cell.BadBeast if distance <= 4 => -400
      case Cell.BadBeast if distance > 4 => -50
      case Cell.BadPlant if distance < 2 => -1000
      case Cell.GoodPlant if distance < 2 => 500
      case Cell.GoodPlant if distance >= 2 => 500 / distance//(150 - distance * 10).max(10)
      case Cell.Wall if distance < 2 => -1000
      case Cell.Wall if distance >= 2 => (-10 / distance) - Random.nextInt(10)
      case Cell.Empty => Random.nextDouble()
      case _ => 0.0
    }
  }

  protected def calculateEffectivenessForSlave(coord: Coord, view: View, distance: Int): Double = {
    view.Absolute(coord) match {
      case Cell.Enemy if distance < 10 => 1000
      case Cell.EnemySlave => 100 / distance
      case Cell.GoodBeast if distance < 2 => 600
      case Cell.GoodBeast if distance >= 2 => 600 / distance//(150 - distance * 10).max(10)
      case Cell.BadBeast if distance <= 4 => 400
      case Cell.BadBeast if distance > 4 => 50
      case Cell.BadPlant if distance < 2 => -100
      case Cell.GoodPlant if distance < 2 => 500
      case Cell.GoodPlant if distance >= 2 => 500 / distance//(150 - distance * 10).max(10)
      case Cell.Wall if distance < 2 => -1000
      case Cell.Wall if distance >= 2 => (-10 / distance) - Random.nextInt(5)
      //case Cell.Empty => Random.nextInt(10)
      //case Cell.Unknown => -10
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





