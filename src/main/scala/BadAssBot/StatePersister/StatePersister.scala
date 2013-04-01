package BadAssBot.StatePersister

import BadAssBot.InternalState
import xml.XML

object MyStatePersister {
  def apply(): MyStatePersister = new ExplicitStatePersister //JsonStatePersister()
}

trait MyStatePersister {

  def save(state: InternalState): framework.Set
  def load(input: String): InternalState
}

class ExplicitStatePersister extends MyStatePersister {

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

class JsonStatePersister extends MyStatePersister {

  def save(state: InternalState): framework.Set = {
    framework.Set(Map(("internalState", sjson.json.Serializer.SJSON.toJSON(state))))
  }

  def load(json: String): InternalState = {

    json match {
      case null => InternalState.empty()
      case "" => InternalState.empty()
      case s => sjson.json.Serializer.SJSON.in[InternalState](s)
    }
  }

}