package BadAssBot

import ActionSelectors.OneWithHighestFactor
import framework._
import Goals._
import Goals.EnemyProximityMinionSpawn
import Goals.RandomMinionSpawn
import Goals.RunFromEnemy
import util.Random
import scala.Some
import framework.Move
import framework.Set


class FoVBot {

  val actionSelector: ActionSelector = OneWithHighestFactor()
  def allShortTermGoals: Seq[Goal] = RunFromEnemy(5) :: Nil
  def allLongTermGoals: Seq[Goal] = MN :: MNE :: ME :: MSE :: MS :: MSW :: MW :: MNW ::Nil
  def allActionGoals: Seq[Goal] = RandomMinionSpawn(0.05) :: EnemyProximityMinionSpawn(10) ::  Nil

  def React(externalState: ExternalState): Seq[MiniOp] = {

    val moveStrategy = slideIfNeeded(selectMovementStrategy(externalState).asInstanceOf[Move], externalState)
    val actionStrategy = selectActionStrategy(externalState)
    val prevMoveState = saveState(externalState, moveStrategy)
    val reloadState = saveStateReload(externalState, actionStrategy)

    moveStrategy :: actionStrategy :: prevMoveState :: reloadState :: Nil
  }

  def selectMovementStrategy(externalState: ExternalState): MiniOp = {

    val shortTermGoals = generateShortTermGoals(externalState)
    val longTermGoals = generateLongTermGoals(externalState)

    actionSelector.selectOneOf(shortTermGoals) match {
      case Some(a: PossibleAction) => a.op
      case None => actionSelector.selectOneOf(longTermGoals).getOrElse(generateWanderingGoal(externalState)).op
    }
  }

  def selectActionStrategy(externalState: ExternalState): MiniOp = {
    val actions = allActionGoals.flatMap(ag => ag.evaluate(externalState))

    actionSelector.selectOneOf(actions) match {
      case Some(a: PossibleAction) => a.op
      case None => NoOp()
    }
  }

  def saveState(externalState: ExternalState, move: MiniOp): MiniOp = {

    val (pmk, pmv) = move match {
      case m : Move => (externalState.name + "_prevMove",  "%d:%d".format(m.direction.x.value, m.direction.y.value))
      case _ => ("", "")
    }
    Set(Map[String, String]((pmk, pmv)))
  }

  def saveStateReload(externalState: ExternalState, action: MiniOp): MiniOp = {

    val newReload = action match {
      case _: EnemyProximityMinionSpawn => 10
      case _ => (externalState.reloadCounter - 1) max 0
    }
    val (rmk, rmv) = ("reloadCounter", "%d".format(newReload))
    Set(Map[String, String]((rmk, rmv)))
  }

  def slideIfNeeded(move: Move, externalState: ExternalState): MiniOp =
    Move(externalState.view.slideIfNeeded(move.direction))

  def generateShortTermGoals(externalState: ExternalState): Seq[PossibleAction] =
    allShortTermGoals.flatMap(stg => stg.evaluate(externalState))

  def generateLongTermGoals(externalState: ExternalState): Seq[PossibleAction] =
    allLongTermGoals.flatMap(atg => atg.evaluate(externalState))


  def generateWanderingGoal(externalState: ExternalState): PossibleAction = {
    Random.nextBoolean() match {
      case true => PossibleAction(Move(externalState.previousMove.toHeading), 1)
      case false => PossibleAction(Move(externalState.view.chooseValidRandomHeading), 1)
    }
  }

}

