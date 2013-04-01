package BadAssBot

import Goals.{BecomeMine, Suicide, GoHome}

class MiniFoVBot extends FoVBot {
  override def allShortTermGoals: Seq[Goal] = GoHome(350) :: BecomeMine(5, 0.05) :: Nil
  override def allActionGoals: Seq[Goal]  = Suicide(4) :: Nil
}