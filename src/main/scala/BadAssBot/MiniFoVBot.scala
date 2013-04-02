package BadAssBot

import Goals.{RandomMinionSpawn, Suicide, GoHome}

class MiniFoVBot extends FoVBot {
  override def allShortTermGoals: Seq[Goal] = GoHome(500) /*:: BecomeMine(5, 0.05)*/ :: Nil
  override def allActionGoals: Seq[Goal]  = Suicide(3) :: RandomMinionSpawn(0.5) :: Nil
}