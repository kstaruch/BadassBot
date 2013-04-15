package BadAssBot

import framework._
import Goals._
import Goals.EnemyProximityMinionSpawn
import Goals.RandomMinionSpawn
import StatePersister.BotStatePersister
import util.Random
import scala.Some
import framework.Move
import framework.Set

class FoVBot {

  val actionSelector: ActionSelector = ActionSelector()
  def allShortTermGoals: Seq[Goal] = ContinueSlide(5) ::  Nil
  def allLongTermGoals: Seq[Goal] = Random.shuffle(MN :: MNE :: ME :: MSE :: MS :: MSW :: MW :: MNW ::Nil)
  def allActionGoals: Seq[Goal] = RandomMinionSpawn(0.8) :: EnemyProximityMinionSpawn(10)  ::  Nil

  def React(externalState: ExternalState): Seq[MiniOp] = {

    val internalState = BotStatePersister().load(externalState.state)

    val moveStrategy = selectMovementStrategy(externalState, internalState).asInstanceOf[Move]
    val actionStrategy = selectActionStrategy(externalState, internalState)
    val reloadState = saveStateReload(externalState, actionStrategy)
    val moveWithSlide = slideIfNeeded(moveStrategy, externalState).asInstanceOf[Move]

    val isSliding = moveStrategy.direction != moveWithSlide.direction

    val newInternalState = InternalState(
      Coord(moveStrategy.direction),
      internalState.reloadCounter + 1,
      internalState.age + 1,
      isSliding match {
        case true => externalState.time
        case false => 0
      })

    val stateToSave = BotStatePersister().save(newInternalState).asInstanceOf[MiniOp]

    moveWithSlide :: actionStrategy :: reloadState :: stateToSave :: Nil
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
      case i if i >= 0 => goals.updated(i, PossibleAction(goals(i).op, goals(i).factor + 100))
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

