package BadAssBot

trait Goal {
  def evaluate(externalState: ExternalState, internalState: InternalState): Option[PossibleAction]
}
