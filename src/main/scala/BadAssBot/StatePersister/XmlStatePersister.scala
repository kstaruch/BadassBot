package BadAssBot.StatePersister

import BadAssBot.InternalState
import xml.XML

class XmlStatePersister extends BotStatePersister {

  def save(state: InternalState): framework.Set = {
    framework.Set(Map(("internalState", state.toXML.toString() )))
  }

  def load(input: String): InternalState = {

    input match {
      case null => InternalState.empty()
      case "" => InternalState.empty()
      case s => InternalState.fromXML(XML.loadString(input))
    }
  }

}
