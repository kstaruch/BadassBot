package BadAssBot

trait ActionSelector {

  def selectOneOf(actions: Seq[PossibleAction]): Option[PossibleAction]
}

