package BadAssBot.StatePersister

import BadAssBot.InternalState

object BotStatePersister {
  def apply(): BotStatePersister = new SimpleStatePersister
}

trait BotStatePersister {

  def save(state: InternalState): framework.Set
  def load(input: Map[String, String]): InternalState
}


