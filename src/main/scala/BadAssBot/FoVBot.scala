package BadAssBot

import framework._
import Goals._
import Goals.EnemyProximityMinionSpawn
import Goals.RandomMinionSpawn
import Goals.RunFromEnemy
import StatePersister.BotStatePersister
import util.Random
import scala.Some
import framework.Move
import framework.Set

class FoVBot {

  val actionSelector: ActionSelector = BadAssBot.ActionSelectors.ActionSelector()
  def allShortTermGoals: Seq[Goal] = RunFromEnemy(5) :: Nil
  def allLongTermGoals: Seq[Goal] = Random.shuffle(MN :: MNE :: ME :: MSE :: MS :: MSW :: MW :: MNW ::Nil)
  def allActionGoals: Seq[Goal] = RandomMinionSpawn(0.5) :: EnemyProximityMinionSpawn(15) ::  Nil

  def React(externalState: ExternalState): Seq[MiniOp] = {

    val internalState = BotStatePersister().load(externalState.internalStateSerialized)

    val moveStrategy = slideIfNeeded(selectMovementStrategy(externalState, internalState).asInstanceOf[Move], externalState)
    val actionStrategy = selectActionStrategy(externalState, internalState)
    val reloadState = saveStateReload(externalState, actionStrategy)

    val newInternalState = InternalState(Coord(moveStrategy.asInstanceOf[Move].direction), internalState.reloadCounter + 1)

    val stateToSave = BotStatePersister().save(newInternalState).asInstanceOf[MiniOp]

    moveStrategy :: actionStrategy :: reloadState :: stateToSave :: Nil
  }

  def selectMovementStrategy(externalState: ExternalState, internalState: InternalState): MiniOp = {

    val shortTermGoals = generateShortTermGoals(externalState, internalState)
    val longTermGoals = generateLongTermGoals(externalState, internalState)

    val enforced = enforcePreviousMovement(longTermGoals, internalState.previousMove)

    actionSelector.selectOneOf(shortTermGoals) match {
      case Some(a: PossibleAction) => a.op
      case None => actionSelector.selectOneOf(enforced).getOrElse(generateWanderingGoal(externalState)).op
    }
  }

  def enforcePreviousMovement(goals: Seq[PossibleAction], previousMove: Coord): Seq[PossibleAction] = {

    goals.indexWhere(pa => pa.op == Move(previousMove.toHeading)) match {
      case i if i >= 0 => goals.updated(i, PossibleAction(goals(i).op, goals(i).factor + 200))
      case _ => goals
    }
  }

  def selectActionStrategy(externalState: ExternalState, internalState: InternalState): MiniOp = {
    val actions = allActionGoals.flatMap(ag => ag.evaluate(externalState, internalState))

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
      case x: Spawn => 1
      case _ => (externalState.reloadCounter - 1) max 0
    }
    val (rmk, rmv) = ("reloadCounter", "%d".format(newReload))
    Set(Map[String, String]((rmk, rmv)))
  }

  def slideIfNeeded(move: Move, externalState: ExternalState): MiniOp =
    Move(externalState.view.slideIfNeeded(move.direction))

  def generateShortTermGoals(externalState: ExternalState, internalState: InternalState): Seq[PossibleAction] =
    allShortTermGoals.flatMap(stg => stg.evaluate(externalState, internalState))

  def generateLongTermGoals(externalState: ExternalState, internalState: InternalState): Seq[PossibleAction] =
    allLongTermGoals.flatMap(atg => atg.evaluate(externalState, internalState))


  def generateWanderingGoal(externalState: ExternalState): PossibleAction = {
    Random.nextBoolean() match {
      case true => PossibleAction(Move(externalState.previousMove.toHeading), 1)
      case false => PossibleAction(Move(externalState.view.chooseValidRandomHeading), 1)
    }
  }

}

