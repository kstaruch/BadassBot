package BadAssBot

trait Goal {
  def evaluate(externalState: ExternalState): Option[PossibleAction]
}
