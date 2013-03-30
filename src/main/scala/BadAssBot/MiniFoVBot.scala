package BadAssBot

import Goals.{Suicide, GoHome}

class MiniFoVBot extends FoVBot {
  override def allShortTermGoals: Seq[Goal] = GoHome(350) :: Nil
  override def allActionGoals: Seq[Goal]  = Suicide(4) :: Nil
}