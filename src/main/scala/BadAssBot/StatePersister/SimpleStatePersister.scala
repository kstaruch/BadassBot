package BadAssBot.StatePersister

import BadAssBot.InternalState
import framework.Coord


class SimpleStatePersister extends BotStatePersister {

  def save(state: InternalState) = {
    framework.Set(Map(
      ("previousMove", state.previousMove.toString),
      ("reloadCounter", state.reloadCounter.toString),
      ("age", state.age.toString),
      ("slideStart", state.slideStart.toString),
      ("role", state.role)
    ))
  }

  def load(input: Map[String, String]) = {

    val previousMove: Coord = Coord.parse(input.getOrElse("previousMove", "0:0"))
    val reloadCounter = input.getOrElse("reloadCounter", "0").toInt
    val age = input.getOrElse("age", "0").toInt
    val slideStart = input.getOrElse("slideStart", "0").toInt
    val role = input.getOrElse("role", "None")

    InternalState(previousMove, reloadCounter, age, slideStart, role)
  }
}
