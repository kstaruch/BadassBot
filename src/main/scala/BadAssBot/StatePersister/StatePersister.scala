package BadAssBot.StatePersister

import BadAssBot.InternalState

object BotStatePersister {
  def apply(): BotStatePersister = new XmlStatePersister
}

trait BotStatePersister {

  def save(state: InternalState): framework.Set
  def load(input: String): InternalState
}


